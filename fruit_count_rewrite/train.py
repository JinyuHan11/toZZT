
import torch

import torch.nn as nn
from torch.optim import Adam
from torch.optim.lr_scheduler import StepLR
from torch.autograd import Variable

import numpy as np

import os
from config import *
from model_build import Res101_SFCN
from data_loading import loading_data
from visual import vis_results
from tensorboardX import SummaryWriter

#设置gpu随机种子
seed = SEED
if seed is not None:
    np.random.seed(seed)
    torch.manual_seed(seed)
    torch.cuda.manual_seed(seed)

# gpus = GPU_ID
gpus = [GPU_ID[0]]
if len(gpus) == 1:
    torch.cuda.set_device(gpus[0])

#提前对卷积算法进行优化
torch.backends.cudnn.benchmark = True



net = Res101_SFCN().cuda()  # 实例化网络,转为gpu

loss_mse = nn.MSELoss().cuda()  # 定义损失函数

optimizer = Adam(net.parameters(), lr=LR, weight_decay=1e-4)  # 定义优化器,学习率初始值为1e-5

scheduler = StepLR(optimizer, step_size=NUM_EPOCH_LR_DECAY, gamma=LR_DECAY)  # 更新lr，lr=lr*gamma^(epoch/step_size)

train_loader, val_loader, restore_transform = loading_data()  # 获取数据生成器



if not os.path.exists(VIS_PATH):
    os.mkdir(VIS_PATH)
mylog = open(LOG_PATH, mode='a+', encoding='utf-8')
writer = SummaryWriter(VIS_PATH)

# 开始训练
for epoch in range(0, MAX_EPOCH):
    if epoch > LR_DECAY_START:
        scheduler.step()
    # train
    for i, data in enumerate(train_loader, 0):
        img, gt_map = data
        img = Variable(img).cuda()
        gt_map = Variable(gt_map).cuda()
        # train
        pred_map = net(img)
        loss = loss_mse(pred_map.squeeze(), gt_map.squeeze())
        loss.backward()
        optimizer.zero_grad()
        optimizer.step()

        # 每10张图片输出一次训练结果
        if (i + 1) % PRINT_FREQ == 0:
            print('train: [ep %d][it %d][loss %4f][lr %4f]' % (
                epoch + 1, i + 1, loss.item(), optimizer.param_groups[0]['lr'] * 10000))

            print('train: [cnt: gt: %.1f pred: %.2f]' % (
                gt_map[0].sum().data / LOG_PARA, pred_map[0].sum().data / LOG_PARA))
    print('=' * 20)
    # validation
    # 每10个epoch验证一次
    if epoch % VAL_FREQ == 0:
        losses = 0
        maes = 0
        mses = 0
        num = 0
        for vi, data in enumerate(val_loader, 0):
            img, gt_map = data

            with torch.no_grad():
                img = Variable(img).cuda()
                gt_map = Variable(gt_map).cuda()

                pred_map = net(img)

                loss = loss_mse(pred_map, gt_map)
                losses = losses + loss
                num = num + 1
                pred_map = pred_map.data.cpu().numpy()
                gt_map = gt_map.data.cpu().numpy()

                for i_img in range(pred_map.shape[0]):
                    pred_cnt = np.sum(pred_map[i_img]) / LOG_PARA
                    gt_count = np.sum(gt_map[i_img]) / LOG_PARA
                maes = maes + abs(gt_count - pred_cnt)
                mses = mses + (gt_count - pred_cnt) * (gt_count - pred_cnt)

                if vi == 0:
                    vis_results(EXP_NAME, epoch, writer, restore_transform, img, pred_map, gt_map)
        print('val: [lossAvg %4f][mae %4f][mse %4f]' %
              (losses / num, maes / num, np.sqrt(mses / num)))
        print('=' * 20)
mylog.close()

'''
class CrowdCounter(nn.Module):
    def __init__(self):
        super(CrowdCounter, self).__init__()


        self.CCN = Res101_SFCN()
        # if len(gpus)>1:
        #    self.CCN = torch.nn.DataParallel(self.CCN,_ids=gpus).cuda()
        # else:
        self.CCN = self.CCN.cuda()
        self.loss_mse_fn = nn.MSELoss().cuda()

    @property
    def loss(self):
        return self.loss_mse

    def forward(self, img, gt_map):
        density_map = self.CCN(img)
        self.loss_mse = self.build_loss(density_map.squeeze(), gt_map.squeeze())
        return density_map

    def build_loss(self, density_map, gt_data):
        loss_mse = self.loss_mse_fn(density_map, gt_data)
        return loss_mse

    def test_forward(self, img):
        density_map = self.CCN(img)
        return density_map

#可视化部分

def update_model(net,optimizer,scheduler,epoch,i_tb,exp_path,exp_name,scores,train_record,log_file=None):

    mae, mse, loss = scores

    snapshot_name = 'all_ep_%d_mae_%.1f_mse_%.1f' % (epoch + 1, mae, mse)

    if mae < train_record['best_mae'] or mse < train_record['best_mse']:
        train_record['best_model_name'] = snapshot_name
        if log_file is not None:
            logger_txt(log_file,epoch,scores)
        to_saved_weight = net.state_dict()
        torch.save(to_saved_weight, os.path.join(exp_path, exp_name, snapshot_name + '.pth'))

    if mae < train_record['best_mae']:
        train_record['best_mae'] = mae
    if mse < train_record['best_mse']:
        train_record['best_mse'] = mse

    latest_state = {'train_record':train_record, 'net':net.state_dict(), 'optimizer':optimizer.state_dict(),\
                    'scheduler':scheduler.state_dict(), 'epoch': epoch, 'i_tb':i_tb, 'exp_path':exp_path, \
                    'exp_name':exp_name}

    torch.save(latest_state,os.path.join(exp_path, exp_name, 'latest_state.pth'))

    return train_record


def logger(exp_path, exp_name, work_dir, exception, resume=False):
    from tensorboardX import SummaryWriter

    if not os.path.exists(exp_path):
        os.mkdir(exp_path)
    writer = SummaryWriter(exp_path + '/' + exp_name)
    log_file = exp_path + '/' + exp_name + '/' + exp_name + '.txt'

    cfg_file = open('../config.py', "r")
    cfg_lines = cfg_file.readlines()

    with open(log_file, 'a') as f:
        f.write(''.join(cfg_lines) + '\n\n\n\n')

    if not resume:
        copy_cur_env(work_dir, exp_path + '/' + exp_name + '/code', exception)

    return writer, log_file


#模型训练

from torch import optim
from torch.autograd import Variable
from torch.optim.lr_scheduler import StepLR

class Trainer():
    def __init__(self, dataloader,pwd):


        self.data_mode = DATASET
        self.exp_name = EXP_NAME
        self.exp_path = EXP_PATH
        self.pwd = pwd

        self.net_name = NET
        self.net = CrowdCounter().cuda()
        self.optimizer = optim.Adam(self.net.CCN.parameters(), lr=LR, weight_decay=1e-4)
        # self.optimizer = optim.SGD(self.net.parameters(), LR, momentum=0.95,weight_decay=5e-4)
        #StepLR进行学习率调整，lr=lr*gamma^(epoch/step_size)
        self.scheduler = StepLR(self.optimizer, step_size=NUM_EPOCH_LR_DECAY, gamma=LR_DECAY)

        self.train_record = {'best_mae': 1e20, 'best_mse': 1e20, 'best_model_name': ''}
        self.timer = {'iter time': Timer(), 'train time': Timer(), 'val time': Timer()}

        self.epoch = 0
        self.i_tb = 0

        if PRE_GCC:
            self.net.load_state_dict(torch.load(PRE_GCC_MODEL))

        self.train_loader, self.val_loader, self.restore_transform = dataloader()

        if RESUME:#是否恢复模型
            latest_state = torch.load(RESUME_PATH)
            self.net.load_state_dict(latest_state['net'])
            self.optimizer.load_state_dict(latest_state['optimizer'])
            self.scheduler.load_state_dict(latest_state['scheduler'])
            self.epoch = latest_state['epoch'] + 1
            self.i_tb = latest_state['i_tb']
            self.train_record = latest_state['train_record']
            self.exp_path = latest_state['exp_path']
            self.exp_name = latest_state['exp_name']
        #保存模型训练日志

        self.writer, self.log_txt = logger(self.exp_path, self.exp_name, self.pwd, 'exp', resume=RESUME)

    def forward(self):

        # self.validate_V3()
        for epoch in range(self.epoch, MAX_EPOCH): #0～MAX_EPOCH
            self.epoch = epoch
            if epoch > LR_DECAY_START:
                self.scheduler.step()

            # training
            self.timer['train time'].tic() #计时开始
            self.train()
            self.timer['train time'].toc(average=False) #计时结束

            print('train time: {:.2f}s'.format(self.timer['train time'].diff)) #输出时间差
            print('=' * 20) #输出’================================‘

            # validation
            if epoch % VAL_FREQ == 0 or epoch > VAL_DENSE_START:
                self.timer['val time'].tic()
                if self.data_mode in ['SHHA', 'SHHB', 'QNRF', 'UCF50']:
                    self.validate_V1()
                self.timer['val time'].toc(average=False)
                print('val time: {:.2f}s'.format(self.timer['val time'].diff))

    def train(self):  # training for all datasets
        self.net.train()
        for i, data in enumerate(self.train_loader, 0):
            self.timer['iter time'].tic()
            img, gt_map = data
            img = Variable(img).cuda()
            gt_map = Variable(gt_map).cuda()

            self.optimizer.zero_grad()
            pred_map = self.net(img, gt_map)
            loss = self.net.loss
            loss.backward()
            self.optimizer.step()

            if (i + 1) % PRINT_FREQ == 0:
                self.i_tb += 1
                self.writer.add_scalar('train_loss', loss.item(), self.i_tb)
                self.timer['iter time'].toc(average=False)
                print('[ep %d][it %d][loss %.4f][lr %.4f][%.2fs]' % \
                      (self.epoch + 1, i + 1, loss.item(), self.optimizer.param_groups[0]['lr'] * 10000,
                       self.timer['iter time'].diff))
                print('        [cnt: gt: %.1f pred: %.2f]' % (
                gt_map[0].sum().data / self.LOG_PARA, pred_map[0].sum().data / self.LOG_PARA))

    def validate_V1(self):  # validate_V1 for SHHA, SHHB, UCF-QNRF, UCF50

        self.net.eval()

        losses = AverageMeter()
        maes = AverageMeter()
        mses = AverageMeter()

        for vi, data in enumerate(self.val_loader, 0):
            img, gt_map = data

            with torch.no_grad():
                img = Variable(img).cuda()
                gt_map = Variable(gt_map).cuda()

                pred_map = self.net.forward(img, gt_map)

                pred_map = pred_map.data.cpu().numpy()
                gt_map = gt_map.data.cpu().numpy()

                for i_img in range(pred_map.shape[0]):
                    pred_cnt = np.sum(pred_map[i_img]) / self.LOG_PARA
                    gt_count = np.sum(gt_map[i_img]) / self.LOG_PARA

                    losses.update(self.net.loss.item())
                    maes.update(abs(gt_count - pred_cnt))
                    mses.update((gt_count - pred_cnt) * (gt_count - pred_cnt))
                if vi == 0:
                    vis_results(self.exp_name, self.epoch, self.writer, self.restore_transform, img, pred_map, gt_map)

        mae = maes.avg
        mse = np.sqrt(mses.avg)
        loss = losses.avg

        self.writer.add_scalar('val_loss', loss, self.epoch + 1)
        self.writer.add_scalar('mae', mae, self.epoch + 1)
        self.writer.add_scalar('mse', mse, self.epoch + 1)

        self.train_record = update_model(self.net, self.optimizer, self.scheduler, self.epoch, self.i_tb, self.exp_path,
                                         self.exp_name, \
                                         [mae, mse, loss], self.train_record, self.log_txt)
        print_summary(self.exp_name, [mae, mse, loss], self.train_record)
'''

