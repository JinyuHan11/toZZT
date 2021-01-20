package com.itheima.springboot_ssm_store.dao;

import com.itheima.springboot_ssm_store.domain.Product;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProductDao {

    List<Product> getHottest();

    List<Product> getNewest();

    Product getProductById(String pid);

    List<Product> getProductByPage(String cid);
}
