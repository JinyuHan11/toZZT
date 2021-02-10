'''
用于获得标准距离
'''
import get_stand_point
from util import *
import os

# 获取标准距离
# ①获得标准图像均值点
# ②依次获取训练图像均值点
# ③求标准均值点与其它各训练图像均值点的距离
# ④求所有均值点的均值，作为标准距离

def get_stand_dist(stand_pic_path,pic_folder_path):
    stand_x,stand_y,stand_z = get_stand_point(stand_pic_path)
    sum_dist = 0
    num_dist = 0
    for file in os.listdir(pic_folder_path):
        file_path = os.path.join(pic_folder_path,file)
        temp_x,temp_y,temp_z = get_stand_point(file_path)
        temp_dist = pp_dist(np.array([stand_x,stand_y,stand_z]),np.array(temp_x,temp_y,temp_z))
        sum_dist = sum_dist+temp_dist
        num_dist = num_dist+1

    #计算平均距离
    mean_dist = round(sum_dist/num_dist,2)

    #返回标准距离
    return mean_dist
