clc; clear all;
dataset = 'B';
standard_size = [768,1024];

att = 'train';

dataset_name = ['shanghaitech_part_' dataset];
path = 'H:/matlab_workspace/gt_test/fruit_transfer/';
output_path = 'H:/matlab_workspace/gt_test/';
train_path_img = strcat(output_path,att, '/img/');
train_path_den = strcat(output_path,att, '/den/');

gt_path = 'H:/matlab_workspace/gt_test/fruit_transfer/';

mkdir(output_path)
mkdir(train_path_img);
mkdir(train_path_den);


num_images = 50;


for idx = 301:350
    i = idx;
    if (mod(idx,10)==0)
        fprintf(1,'Processing %3d/%d files\n', idx, num_images);
    end
    load(strcat(gt_path, num2str(i),'.mat')) ;
    input_img_name = strcat(path,num2str(i),'.jpg');
    im = imread(input_img_name);  
    [h, w, c] = size(im);
    annPoints =  gt_point


    rate = standard_size(1)/h;
    rate_w = w*rate;
    if rate_w>standard_size(2)
        rate = standard_size(2)/w;
    end
    rate_h = double(int16(h*rate))/h;
    rate_w = double(int16(w*rate))/w;
    im = imresize(im,[int16(h*rate),int16(w*rate)]);
    annPoints(:,1) = annPoints(:,1)*double(rate_w);
    annPoints(:,2) = annPoints(:,2)*double(rate_h);
    
    im_density = get_density_map_gaussian(im,annPoints,15,4); 
    im_density = im_density(:,:,1);
    
    imwrite(im, [train_path_img num2str(idx) '.jpg']);
    csvwrite([train_path_den num2str(idx) '.csv'], im_density);
end

