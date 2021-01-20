package com.itheima.springboot_ssm_store.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.springboot_ssm_store.dao.AdminDao;
import com.itheima.springboot_ssm_store.domain.Category;
import com.itheima.springboot_ssm_store.domain.PageBean;
import com.itheima.springboot_ssm_store.domain.Product;
import com.itheima.springboot_ssm_store.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private AdminDao adminDao;


    @Override
    public List<Category> findAllCategory() {
        return adminDao.findAllCategory();

    }

    @Override
    public boolean deleteCategoryById(String cid) {
        int a = adminDao.deleteCategoryById(cid);

        System.out.println("删除返回值："+a);
        if(a>=0){
            return true;
        }
        return false;
        //System.out.println("删除种类返回值："+a);

    }

    @Override
    public int getRows() {
        return adminDao.getRows();
    }

    @Override
    public boolean addCategory(String newCid, String cname) {
        int a =  adminDao.addCategory(newCid,cname);
        System.out.println("添加种类返回值："+a);
        if(a >= 0){
            return  true;
        }
        return false;
    }

    @Override
    public Category findCategoryById(String cid) {
        return adminDao.findCategoryById(cid);
    }

    @Override
    public boolean editCategoryById(String cid, String cname) {
        int a = adminDao.editCategoryById(cid,cname);
        System.out.println("修改种类返回值："+a);
        if(a>=0){
            return true;
        }
        return false;
    }

    @Override
    public PageBean<Product> findProductByPage(int currentPage) {
        //1.开启分页 ，基于拦截器技术，拦截查询的sql，动态的加入 limit
        Page<Object> pageObject = PageHelper.startPage(currentPage, 12);
        //2.查询数据 根据分类id查询所有数据
        List<Product> productList = adminDao.findProductByPage();

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

    @Override
    public int getProductRows() {
        return adminDao.getProductRows();
    }

    @Override
    public boolean addProduct(Product product) {
        int a = adminDao.addProduct(product);
        if(a>=0){
            return true;
        }
        return false;
    }
}
