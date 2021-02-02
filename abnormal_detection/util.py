'''
存放工具方法


'''
import numpy as np
import scipy
import os

def all_mean(folderPath):  # folderPath:数据集路径
    img_lists = os.listdir(folderPath)
    # print('image_lists:',img_lists)
    '''
    R_means = []
    G_means = []
    B_means = []

    for img in img_lists:
        imgPath = os.path.join(folderPath,img)
        im = cv2.imread(imgPath)#默认读取BGR
        im_R = im[:,:,2]
        im_G = im[:,:,1]
        im_B = im[:,:,0]

        im_R_mean = np.mean(im_R)
        im_G_mean = np.mean(im_G)
        im_B_mean = np.mean(im_B)

        R_means.append(im_R_mean)
        G_means.append(im_G_mean)
        B_means.append(im_B_mean)

    rMean = np.mean(R_means)
    gMean = np.mean(G_means)
    bMean = np.mean(B_means)

    return rMean,gMean,bMean
'''
    means = []
    for img in img_lists:
        imgPath = os.path.join(folderPath, img)
        image = scipy.misc.imread(imgPath)
        mean = np.mean(image)
        means.append(mean)

    mean = np.mean(means)
    return mean


def standard(image):  # (x-mean)/sd
    mean = np.mean(image)
    var = np.mean(np.square(image - mean))
    image = (image - mean) / np.sqrt(var)

    return image


def standard_useAllMean(image, folderPath):
    mean = all_mean(folderPath)
    var = np.mean(np.square(image - mean))
    image = (image - mean) / np.sqrt(var)

    return image
