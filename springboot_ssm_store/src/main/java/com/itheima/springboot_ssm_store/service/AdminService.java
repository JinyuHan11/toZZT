package com.itheima.springboot_ssm_store.service;

import com.itheima.springboot_ssm_store.domain.Category;
import com.itheima.springboot_ssm_store.domain.PageBean;
import com.itheima.springboot_ssm_store.domain.Product;

import java.util.List;


public interface AdminService {

    List<Category> findAllCategory();

    boolean deleteCategoryById(String cid);

    int getRows();

    boolean addCategory(String newCid, String cname);

    Category findCategoryById(String cid);

    boolean editCategoryById(String cid,String cname);

    PageBean<Product> findProductByPage(int parseInt);

    int getProductRows();

    boolean addProduct(Product product);
}
