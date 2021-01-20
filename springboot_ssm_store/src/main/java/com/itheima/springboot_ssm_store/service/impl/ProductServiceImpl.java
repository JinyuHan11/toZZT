package com.itheima.springboot_ssm_store.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.springboot_ssm_store.dao.ProductDao;
import com.itheima.springboot_ssm_store.domain.PageBean;
import com.itheima.springboot_ssm_store.domain.Product;
import com.itheima.springboot_ssm_store.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductDao productDao;

    @Override
    public List<Product> getHottestProducts() {
        List<Product> hottest = productDao.getHottest();
        for (Product product:
                hottest) {
            System.out.println("shop_price:"+product.getShop_price());
        }
        //return productDao.getHottest();
        return hottest;
    }

    @Override
    public List<Product> getNewestProducts() {

        return productDao.getNewest();
    }

    @Override
    public Product getProductById(String pid) {

        return productDao.getProductById(pid);
    }

    @Override
    public PageBean<Product> getProductByPage(String cid, int currentPage) {
        //1.开启分页 ，基于拦截器技术，拦截查询的sql，动态的加入 limit ？，？
        Page<Object> pageObject = PageHelper.startPage(currentPage, 12);
        //2.查询数据 根据分类id查询所有数据
        List<Product> productList = productDao.getProductByPage(cid);

        //创建pageBean，把其属性 赋值
        PageBean<Product> pageBean  = new PageBean<Product>();
        //分页后的数据
        pageBean.setList(productList);
        //分页后的当前页
        pageBean.setCurrentPage(pageObject.getPageNum());
        pageBean.setPageSize(12);
        //总数量
        pageBean.setTotalCount(pageObject.getTotal());
        //分页后的总页数
        pageBean.setTotalPage(pageObject.getPages());

        return pageBean;
    }

}
