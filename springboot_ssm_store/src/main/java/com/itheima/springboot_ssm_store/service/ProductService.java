package com.itheima.springboot_ssm_store.service;

import com.itheima.springboot_ssm_store.domain.PageBean;
import com.itheima.springboot_ssm_store.domain.Product;

import java.util.List;

public interface ProductService {

    List<Product> getHottestProducts();

    List<Product> getNewestProducts();

    Product getProductById(String pid);

    PageBean<Product> getProductByPage(String cid, int currentPage);
}
