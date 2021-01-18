import time


# 各类参数设置
'''
数据集处理参数
'''
# 标准大小
STD_SIZE = (768, 1024)
# 训练集大小
TRAIN_SIZE = (576, 768)

########################不同数据集需要修改常量##################
# 数据集路径
DATA_PATH = 'C:/Users/50106/Desktop/fruit_number/ProcessedData1'
# 可视化图像存储路径
#VIS_PATH = "C:/Users/50106/Desktop/fruit_number/runs/exp1"
VIS_PATH = "H:/upload"
# 训练日志路径
LOG_PATH = "H:/upload/log.txt"
DATASET = 'FRUIT'
#训练轮数
MAX_EPOCH = 30
# 打印训练过程频率
PRINT_FREQ = 10
#验证频率
VAL_FREQ = 10
TRAIN_BATCH_SIZE = 1
VAL_BATCH_SIZE = 1
#学习率设置
LR = 1e-5  # learning rate
LR_DECAY = 0.995  # decay rate
LR_DECAY_START = -1  # when training epoch is more than it, the learning rate will be begin to decay
NUM_EPOCH_LR_DECAY = 1  # decay frequency
############################################################



###########################定值常量,暂时不需要更改###############
# 图像正则化参数，使用ImageNet所得参数，实用性和效果较好
MEAN_STD = ([0.452016860247, 0.447249650955, 0.431981861591], [0.23242045939, 0.224925786257, 0.221840232611])
GPU_ID = [0, 1]
SEED = 3035
LABEL_FACTOR = 1
#密度图放大因子
LOG_PARA = 100.

RESUME_MODEL = ''

NET = 'Res101_SFCN'

PRE_GCC = False  # use the pretrained model on GCC dataset
PRE_GCC_MODEL = 'path to model'  # path to model

RESUME = False  # contine training
RESUME_PATH = './exp/2020-12-2/latest_state.pth'  #

LAMBDA_1 = 1e-4  # SANet:0.001 CMTL 0.0001

now = time.strftime("%m-%d_%H-%M", time.localtime())

EXP_NAME = now + '_' +DATASET + '_' + NET + '_' + str(LR)

EXP_PATH = '../exp'  # the path of logs, checkpoints, and current codes


VAL_DENSE_START = 50

VISIBLE_NUM_IMGS = 1  # must be 1 for training images with the different sizes
