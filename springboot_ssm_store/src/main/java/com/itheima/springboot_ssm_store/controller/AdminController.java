package com.itheima.springboot_ssm_store.controller;


import com.itheima.springboot_ssm_store.domain.Category;
import com.itheima.springboot_ssm_store.domain.PageBean;
import com.itheima.springboot_ssm_store.domain.Product;
import com.itheima.springboot_ssm_store.service.AdminService;
import com.itheima.springboot_ssm_store.utils.RequestToProduct;
import com.itheima.springboot_ssm_store.utils.Result;
import com.itheima.springboot_ssm_store.utils.UUIDUtils;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
public class AdminController {

    @Autowired
    private AdminService adminService;

    @RequestMapping("/admin")
    public Result adminMethod(String method,String cid,String cname,String currentPage){
        System.out.println("进入admin方法");
        if("findAllCategory".equals(method)){//查询所有类
            List<Category> categoryList = adminService.findAllCategory();
            return  new Result(Result.SUCCESS,"查询所有种类成功！",categoryList);
        }else if("deleteCategoryById".equals(method)){//删除选定种类
                if(adminService.deleteCategoryById(cid)){
                    //删除成功时：
                    return  new Result(Result.SUCCESS,"删除成功！");
                }
                //删除失败时：
                return new Result(Result.FAILS,"删除失败！");

        }else if("addCategory".equals(method)){
            int rows = adminService.getRows();
            String newCid = Integer.toString(rows+1);
            System.out.println("newCid:"+newCid);
            if(adminService.addCategory(newCid,cname)){
                return  new Result(Result.SUCCESS,"添加种类成功！");
            }
            return  new Result(Result.SUCCESS,"添加种类失败！");
        }else if("findCategoryById".equals(method)){
            Category category = adminService.findCategoryById(cid);
            return new Result(Result.SUCCESS,"按cid查询种类成功",category);

        }else if("editCategoryById".equals(method)){
            if(adminService.editCategoryById(cid,cname)){
                return new Result(Result.SUCCESS,"更新种类成功");
            }
            return new Result(Result.FAILS,"更新种类失败");

        }else if("findProductByPage".equals(method)){
            //分页展示产品
            PageBean<Product> pageBean =  adminService.findProductByPage(Integer.parseInt(currentPage));
            return new Result(Result.SUCCESS,"获取所有产品信息成功！",pageBean);
        }
    return null;
    }
    //传来的参数缺少pdate，pflag
    @RequestMapping("/productUpload")
    public Result productUpload(String method,  MultipartFile file, HttpServletRequest request) throws IOException, FileUploadException {

        if("addProduct".equals(method)){
            int rows = adminService.getProductRows();
            String pid = Integer.toString(rows+1);
            //获取当前时间
            Date dt = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentTime = sdf.format(dt);
            int pflag = 0;
            StandardMultipartHttpServletRequest req = (StandardMultipartHttpServletRequest)request;

            //获取文件
            MultipartFile multipartFile = req.getFile("pimage");
            //获取文件原名orignFileName;
            String orignFileName = multipartFile.getOriginalFilename();
            System.out.println("orignFileName:"+orignFileName);
            //重命名文件名
            String fileName = UUIDUtils.getUUID().replace("-","")+"_"+orignFileName;
            //图片保存路径
            //String uploadDir = "H:/upload/";
            String uploadDir = "E:/Downloads/山东农业大学实训材料/黑马商城讲义资料/讲义代码/06/02-源代码/02_实现代码/web/resources/products/1/";
            //创建文件对象
            File uploadFile = new File(uploadDir,fileName);
            //保存文件到对应文件夹下
            InputStream inputStream = multipartFile.getInputStream();
            FileOutputStream fos = new FileOutputStream(uploadFile);
            IOUtils.copy(inputStream,fos);
            //图片提取路径
            String abstractDir = "resources/products/1/"+fileName;
            //String completeFileName = uploadDir+fileName;
            //将各种产品信息存入产品对象
            Product product = RequestToProduct.requestToProduct(request,abstractDir,pid,currentTime);

            if(adminService.addProduct(product)){
                return new Result(Result.SUCCESS,"添加产品成功！");
            }
            return new Result(Result.FAILS,"添加产品失败");
        }
        return null;
    }

}
