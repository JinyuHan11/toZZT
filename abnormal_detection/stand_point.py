'''
获取标准参考图像标准点，即各维度均值
'''

import scipy
from util import *

#获取各维度平均值，取消之前（max+min）/2的方法
def get_stand_point(x_array,y_array,z_array):
    len_x = len(x_array)
    len_y = len(y_array)
    len_z = len(z_array)
    sum_x = 0
    sum_y = 0
    sum_z = 0

    for temp_value in x_array:
        sum_x = sum_x+temp_value
    for temp_value in y_array:
        sum_y = sum_y+temp_value
    for temp_value in z_array:
        sum_z = sum_z+temp_value

    #保留两位小数
    stand_x = round(sum_x/len_x,2)
    stand_y = round(sum_y/len_y,2)
    stand_z = round(sum_z/len_z,2)

    #返回标准点
    return stand_x,stand_y,stand_z

#获取各维度数组
def abstract_rgb(pic_path,meanSwitch=True,folderPath=None):
    '''
    meanSwitch=True:使用单个数据集mean
    meanSwitch=False:使用整个数据集mean
    '''
    image_3d = scipy.misc.imread(pic_path)
    if meanSwitch == False:
        #print('meanSwitch==False')
        if folderPath == None:
            raise ValueError("folderPath can not be None!")
        image = standard_useAllMean(image_3d,folderPath)
    else:
        #print('meanSwitch==True')
        image = standard(image_3d)
    x = []
    y = []
    z = []
    x_size = image.shape[0]
    y_size = image.shape[1]
    #z_size = image.shape[2]

    #image = image/255 #取消归一化
    for i in range(x_size):
        for j in range(y_size):
            x.append(image[i][j][0])
    for i in range(x_size):
        for j in range(y_size):
            y.append(image[i][j][1])
    for i in range(x_size):
        for j in range(y_size):
            z.append(image[i][j][2])

    x_array = np.array(x)
    y_array = np.array(y)
    z_array = np.array(z)
    #print('xArray:',len(x_array))
    #print('xArray:',x_array)
    return get_stand_point(x_array,y_array,z_array)







