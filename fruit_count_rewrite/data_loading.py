from config import *
from img_preprocessing_utils import *
import torchvision.transforms as standard_transforms
from torch.utils.data import DataLoader
from make_dataset import FRUIT

def loading_data():
    mean_std = MEAN_STD #正则化参数
    log_para = LOG_PARA #密度图放大因子
    #训练集预处理方式
    train_main_transform = Compose([
        RandomCrop(TRAIN_SIZE),
        RandomHorizontallyFlip()  # 随机水平倾斜
    ])
    #验证集预处理方式
    val_main_transform = Compose([
        RandomCrop(TRAIN_SIZE)  # 随机裁剪
    ])
    #val_main_transform = None

    img_transform = standard_transforms.Compose([
        standard_transforms.ToTensor(),
        standard_transforms.Normalize(*mean_std)  #图像正则化，减少噪音影响，提升有效信息显示效果
    ])

    gt_transform = standard_transforms.Compose([
        LabelNormalize(log_para)  # 放大100倍
    ])

    restore_transform = standard_transforms.Compose([
        DeNormalize(*mean_std),
        standard_transforms.ToPILImage()
    ])

    #获取图像数据集
    train_set = FRUIT(DATA_PATH + '/train', 'train', main_transform=train_main_transform, img_transform=img_transform,
                     gt_transform=gt_transform)
    val_set = FRUIT(DATA_PATH + '/test', 'test', main_transform=val_main_transform, img_transform=img_transform,
                    gt_transform=gt_transform)

    #num_workers:数据导入进程数
    #drop_last:是否丢弃最后一批数据，该批数据可能不足batch_size大小
    train_loader = DataLoader(train_set, batch_size=TRAIN_BATCH_SIZE, num_workers=0, shuffle=True, drop_last=True)
    val_loader = DataLoader(val_set, batch_size=VAL_BATCH_SIZE, num_workers=0, shuffle=True, drop_last=True)

    return train_loader, val_loader, restore_transform

