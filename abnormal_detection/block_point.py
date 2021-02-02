'''
提取图像块均值点
步骤：
①通过opencv截取图像
②计算各维度均值点

'''
from config import *
import cv2

img = cv2.imread("C:/Users/50106/Desktop/1.jpg")  #获取到ndarray数组,(355,500,3)
print(img.shape[0])
# new_img = img[100:300,100:300]  #先高，后宽
# cv2.imwrite("C:/Users/50106/Desktop/2.jpg",new_img)
# cv2.waitKey(0)


#从图像中截取BLOCK_SIZE大小的区域，计算均值点
def get_img_block(pic_path):
    img = cv2.imread(pic_path)
    img_height = img.shape[0]
    img_width = img.shape[1]

    #开始提取固定大小区域


    pass