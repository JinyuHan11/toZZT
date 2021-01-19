import numpy as np

import torch
from torch import optim
from torch.autograd import Variable
from torch.optim.lr_scheduler import StepLR

from FC import FruitCounter
from config import *
from utils import *
import pdb


class Trainer():
    def __init__(self, dataloader, work_dir):

        self.data_mode = DATASET
        self.exp_name = EXP_NAME
        self.exp_path = EXP_PATH
        self.work_dir = work_dir

        self.net_name = NET
        self.net = FruitCounter(GPU_ID,self.net_name).cuda()
        self.optimizer = optim.Adam(self.net.CCN.parameters(), lr=LR, weight_decay=1e-4)
        # self.optimizer = optim.SGD(self.net.parameters(), cfg.LR, momentum=0.95,weight_decay=5e-4)
        self.scheduler = StepLR(self.optimizer, step_size=NUM_EPOCH_LR_DECAY, gamma=LR_DECAY)

        self.train_record = {'best_mae': 1e20, 'best_mse':1e20, 'best_model_name': ''}
        self.timer = {'iter time' : Timer(),'train time' : Timer(),'val time' : Timer()} 

        self.epoch = 0
        self.i_tb = 0

        self.train_loader, self.val_loader, self.restore_transform = dataloader()
        self.writer, self.log_txt = logger(self.exp_path, self.exp_name, self.work_dir, 'exp', resume=RESUME)


    def forward(self):

        # self.validate_V3()
        for epoch in range(self.epoch,MAX_EPOCH):
            self.epoch = epoch
            if epoch > LR_DECAY_START:
                self.scheduler.step()
                
            # training    
            self.timer['train time'].tic()
            self.train()
            self.timer['train time'].toc(average=False)

            print( 'train time: {:.2f}s'.format(self.timer['train time'].diff) )
            print( '='*20 )

            # validation
            if epoch%VAL_FREQ==0 or epoch>VAL_DENSE_START:
                self.timer['val time'].tic()
                self.validate_V1()
                self.timer['val time'].toc(average=False)
                print( 'val time: {:.2f}s'.format(self.timer['val time'].diff) )


    def train(self):
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
                print( '[ep %d][it %d][loss %.4f][lr %.4f][%.2fs]' % \
                        (self.epoch + 1, i + 1, loss.item(), self.optimizer.param_groups[0]['lr']*10000, self.timer['iter time'].diff) )
                print( '        [cnt: gt: %.1f pred: %.2f]' % (gt_map[0].sum().data/LOG_PARA, pred_map[0].sum().data/LOG_PARA) )


    def validate_V1(self):

        self.net.eval()
        
        losses = AverageMeter()
        maes = AverageMeter()
        mses = AverageMeter()

        for vi, data in enumerate(self.val_loader, 0):
            img, gt_map = data

            with torch.no_grad():
                img = Variable(img).cuda()
                gt_map = Variable(gt_map).cuda()

                pred_map = self.net.forward(img,gt_map)

                pred_map = pred_map.data.cpu().numpy()
                gt_map = gt_map.data.cpu().numpy()

                for i_img in range(pred_map.shape[0]):
                
                    pred_cnt = np.sum(pred_map[i_img])/self.cfg_data.LOG_PARA
                    gt_count = np.sum(gt_map[i_img])/self.cfg_data.LOG_PARA

                    
                    losses.update(self.net.loss.item())
                    maes.update(abs(gt_count-pred_cnt))
                    mses.update((gt_count-pred_cnt)*(gt_count-pred_cnt))
                if vi==0:
                    vis_results(self.exp_name, self.epoch, self.writer, self.restore_transform, img, pred_map, gt_map)
            
        mae = maes.avg
        mse = np.sqrt(mses.avg)
        loss = losses.avg

        self.writer.add_scalar('val_loss', loss, self.epoch + 1)
        self.writer.add_scalar('mae', mae, self.epoch + 1)
        self.writer.add_scalar('mse', mse, self.epoch + 1)

        self.train_record = update_model(self.net,self.optimizer,self.scheduler,self.epoch,self.i_tb,self.exp_path,self.exp_name, \
            [mae, mse, loss],self.train_record,self.log_txt)
        print_summary(self.exp_name,[mae, mse, loss],self.train_record)






