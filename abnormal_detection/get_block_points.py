'''
提取图像块均值点
步骤：
①通过opencv截取图像
②计算各维度均值点

'''
from config import *
import cv2
import json

img = cv2.imread("C:/Users/50106/Desktop/1.jpg")  #获取到ndarray数组,(355,500,3)
print(img.shape[0])
new_img = img[100:300,100:300]  #先高，后宽
print(type(new_img))
print(new_img.shape)
test_dict = {}
test_dict['0_1'] = new_img
print(test_dict)
# cv2.imwrite("C:/Users/50106/Desktop/2.jpg",new_img)
# cv2.waitKey(0)


#从图像中截取BLOCK_SIZE大小的区域，保存为json文件
def get_img_block(pic_path):
    img = cv2.imread(pic_path)
    img_height = img.shape[0]
    img_width = img.shape[1]
    num_height = int(img_height/BLOCK_SIZE)
    num_width = int(img_width/BLOCK_SIZE)

    img_block_dict = {} # 用于存放图像块数据
    for i in range(num_height):
        for j in range(num_width):
            #提取固定大小区域，将其按照json数据串形式进行保存：①先保存为dict，{‘i0_j0’:'array0','i1_j1':'array1'}
            # ②通过json.dumps转为json,并保存为文件
            temp_img = img[i*BLOCK_SIZE:i*BLOCK_SIZE+BLOCK_SIZE,j*BLOCK_SIZE:j*BLOCK_SIZE+BLOCK_SIZE]
            dict_name = str(i)+'_'+str(j)
            img_block_dict[dict_name] = temp_img
            #返回字典中键值对数量
            print("len of img_block_dict:%s"%len(img_block_dict))
            #将图像块数据保存为json文件,之后使用json.load读取
            with open(JSON_FILE,'w',encoding='utf-8') as f:
                f.write(json.dumps(img_block_dict,ensure_ascii=False))

#计算各图像块均值点函数并进行保存：①从json文件提取图像块数组
# ②计算均值点
# ③将坐标-点对再存入字典，形式为：{'i_j':[float1,float2,float3]}
# ④将字典再此存为json文件

