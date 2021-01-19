# 数据集制作
'''
数据集制作类
'''
import torch
import os
from torch.utils import data
from PIL import Image
import pandas as pd
import numpy as np

class FRUIT(data.Dataset):
    def __init__(self, data_path, mode, main_transform=None, img_transform=None, gt_transform=None):
        self.img_path = data_path + '/img' #图像路径
        self.gt_path = data_path + '/den' #密度图路径
        self.data_files = [filename for filename in os.listdir(self.img_path)
                           if os.path.isfile(os.path.join(self.img_path, filename))]
        self.num_samples = len(self.data_files) #样本数量
        self.main_transform = main_transform #预处理方式
        self.img_transform = img_transform #图像预处理方式
        self.gt_transform = gt_transform #密度图预处理方式

    #数据集生成函数，作为后续提取数据的入口
    def __getitem__(self, index):
        fname = self.data_files[index]
        #获取img,den的ImageFile对象
        img, den = self.read_image_and_gt(fname)
        if self.main_transform is not None:
            img, den = self.main_transform(img, den)
        if self.img_transform is not None:
            img = self.img_transform(img)  # 对图像进行归一化
        if self.gt_transform is not None:
            den = self.gt_transform(den)
        return img, den

    def __len__(self):
        return self.num_samples

    def read_image_and_gt(self, fname):
        img = Image.open(os.path.join(self.img_path, fname))

        #图像为黑白的情况
        if img.mode != 'RGB':
            img = img.convert('RGB')

        #将密度图转化为图像
        den = pd.read_csv(os.path.join(self.gt_path, os.path.splitext(fname)[0] + '.csv'), sep=',', header=None).values
        den = den.astype(np.float32, copy=False)
        den = Image.fromarray(den)
        return img, den

    def get_num_samples(self):
        return self.num_samples

