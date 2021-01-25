%对图片上某个点进行标注
clear all
close all
clc
set(gcf,'color','white')
src='';
for i=1:10
    num = i;
    str = '.jpg' ;
    Mstr='.mat';
    
    %%%%%%%%%%%%%%
    filestr='C:/Users/lx/Desktop/数据预处理/吴志超/img/';
    refilestr='C:/Users/lx/Desktop/数据预处理/吴志超/transfer/';
    %%%%%%%%%%%%%%%%%
    
    %left_par='air(';
    %Mark='mark';
    %right_par=')';
    STR = sprintf('%s%d%s', filestr,num , str) ;
    RSTR=sprintf('%s%d%s', refilestr,num , str) ;
    MSTR=sprintf('%s%d%s', refilestr,num ,Mstr) ;
    MarkSTR=sprintf('%s%d%s', refilestr,num ,Mstr) ;
    %pic_source=imread(STR); 
    pic = imread(STR);
    %pic=imresize(pic_source,[602,800]);
    %pic=pic_source;
    imwrite(pic,RSTR);
    showpic=imshow(pic);
    %set(B,  'X', [0 1000], 'Y', [0 1000])
    [x1,y1] = ginput;
    B=[x1,y1];
    hold on
    plot(x1,y1,'r+');
    disp(B);
    
    [x2,y2] = ginput;
    C=[x2,y2];
    hold on
    plot(x2,y2 ,'r+');
    disp(C);
    
%     [x3,y3] = ginput;
%     D=[x3,y3];
%     hold on
%     plot(x3,y3,'r+');
% 
%     [x4,y4] = ginput;
%     E=[x4,y4];
%     hold on
%     plot(x4,y4,'r+');
%   

    %gt_point=[B;C;D;E];
    gt_point = [B;C];
    disp(gt_point);
    save(MSTR,'gt_point');   
end