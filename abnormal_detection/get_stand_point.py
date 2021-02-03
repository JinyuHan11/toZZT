'''
获取标准参考图像标准点，即各维度均值
'''

import scipy
from util import *




#获取各维度数组,获取各维度平均值点，取消之前（max+min）/2的方法
def get_stand_point(pic_path,meanSwitch=True,folderPath=None):
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
    return get_mean_point(x_array,y_array,z_array)










