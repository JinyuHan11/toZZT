'''
进行计算，确定异常图像
步骤：①获取标准距离
②获取要检测图像的图像块均值点字典
③比较检测距离与标准距离*a的大小关系，进行判断，确定异常区域
'''


from config import *
import get_stand_dist
import get_block_points
import os
import json
from util import pp_dist
import get_stand_point
import numpy as np

if __name__=="__main__":

    # 获取标准点
    stand_x,stand_y,stand_z = get_stand_point(stand_pic_path)

    # 获取标准距离
    stand_dist = get_stand_dist(stand_pic_path,pic_folder_path)
    #遍历要检测的图像
    for i,file in enumerate(os.listdir(pred_folder_path)):
        file_path = os.path.join(pred_folder_path,file)

        #保存图像块点到json文件，便于后期作图处理
        get_block_points(file_path,i)

        #读取图像块均值点json文件
        POINT_JSON_FILE = POINT_JSON_FILE_ROOT+'point_json_'+str(i)+'.json'
        img_block_points = {}
        with open(POINT_JSON_FILE,'w',encoding='utf-8') as f:
            img_block_points = json.load(f)
            f.close()
        # 计算均值点与标准点之间距离  与标准距离*a的关系

        img_block_point_keys = img_block_points.keys()
        img_block_point_values = img_block_points.values()

        # 记录异常区域用列表
        abnormal_areas = []
        for j,img_block_point_value in enumerate(img_block_point_values):
            temp_dist = pp_dist(np.array([stand_x,stand_y,stand_z]),np.array([img_block_point_value[0],
                                                                              img_block_point_value[1],
                                                                              img_block_point_value[2]]))
            if temp_dist>stand_dist*DIST_RATE:
                abnormal_areas.append(j)
                print('此图像存在异常区域')

        #将异常坐标区域保存为json文件，方便后续使用
        BLOCK_JSON_FILE = BLOCK_JSON_FILE_ROOT + 'block_json_' + str(i) + '.json'
        img_blocks = {}
        with open(BLOCK_JSON_FILE,'r',encoding='utf-8') as f:
            img_blocks = json.load(f)
            f.close()

        img_blocks_keys = img_blocks.keys()
        img_blocks_values = img_blocks.values()

        abn_areas_dict = {}
        for area in abnormal_areas:
            abn_areas_dict[img_blocks_keys[area]] = img_blocks_values[area]

        abn_areas_file = ABN_AREAS_FILE_ROOT+'abn_block_json_'+str(i)+'.json'
        with open(abn_areas_file,'w',encoding='utf-8') as f:
            json.dumps(abn_areas_dict,ensure_ascii=False)
            f.close()

        # 后续对异常区域进行分析










    pass


