package com.itheima.springboot_ssm_store.utils;


import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class ProductUploadUtil {

    public static Map<String,String[]> getMap(HttpServletRequest request){
        Map<String,String[]> map = new HashMap<String, String[]>();
        try {
            //创建磁盘工厂对象
            DiskFileItemFactory itemFactory = new DiskFileItemFactory();
            //创建Servlet的上传解析对象,构造方法中,传递磁盘工厂对象
            ServletFileUpload fileUpload = new ServletFileUpload(itemFactory);
            /*
             * fileUpload调用方法 parseRequest,解析request对象
             * 页面可能提交很多内容 文本框,文件,菜单,复选框 是为FileItem对象
             * 返回集合,存储的文件项对象
             */
            /*
            List<FileItem> list = fileUpload.parseRequest(request);
            for(FileItem item : list){
                //判断普通项还是附件项
                if(item.isFormField()){
                    //取出普通项的name属性值  name="username"
                    String name = item.getFieldName();
                    //取出普通的填写的值
                    String value = item.getString("utf-8");
                    map.put(name,new String[]{value});
                }else {
                    //附件项
                }
            }*/
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return  map;
    }

}
