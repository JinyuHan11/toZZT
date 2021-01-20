package com.itheima.springboot_ssm_store.controller;

import com.itheima.springboot_ssm_store.domain.PageBean;
import com.itheima.springboot_ssm_store.domain.Product;
import com.itheima.springboot_ssm_store.service.ProductService;
import com.itheima.springboot_ssm_store.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    @RequestMapping("/product")   //getHottestAndNewest
    public Result productMethod(String method,String pid,String cid,String currentPage){
        if("getHottestAndNewest".equals(method)){
            //首页里 查询 最新最热的商品
            Map<String, List<Product>> map = new HashMap<>();
            //最热卖：is_hot = 1
            List<Product> hottest = productService.getHottestProducts();

            map.put("hottestList",hottest);
            //最新：按照日期降序 pdate
            List<Product> newest =  productService.getNewestProducts();
            map.put("newestList",newest);
            return  new Result(Result.SUCCESS,"查询最新最热商品成功",map);
        }else if("getProductById".equals(method)){
            //通过id查询商品信息
            Product product = productService.getProductById(pid);
            return  new Result(Result.SUCCESS,"查询商品详情成功!",product);
        }else if("getProductByPage".equals(method)){
            //通过分类查询对应的商品列表  cid: 3
            //currentPage: 1
            PageBean<Product> pageBean =  productService.getProductByPage(cid,Integer.parseInt(currentPage));

            return new Result(Result.SUCCESS,"查询分页成功",pageBean);
        }
        return null;
    }

}
