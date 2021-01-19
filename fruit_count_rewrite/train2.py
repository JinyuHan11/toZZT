import os
import numpy as np
import torch
from data_loading import loading_data
from trainer import Trainer
from config import *

seed = SEED
if seed is not None:
    np.random.seed(seed)
    torch.manual_seed(seed)
    torch.cuda.manual_seed(seed)

gpus = GPU_ID
if len(gpus)==1:
    torch.cuda.set_device(gpus[0])

#训练前准备合适的卷积算法
torch.backends.cudnn.benchmark = True


cc_trainer = Trainer(loading_data,WORK_DIR)
cc_trainer.forward()
