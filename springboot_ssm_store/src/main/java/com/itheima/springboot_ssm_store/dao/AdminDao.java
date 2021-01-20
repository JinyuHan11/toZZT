package com.itheima.springboot_ssm_store.dao;

import com.itheima.springboot_ssm_store.domain.Category;
import com.itheima.springboot_ssm_store.domain.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AdminDao {


    List<Category> findAllCategory();

    int deleteCategoryById(String cid);

    int getRows();

    int addCategory(@Param("newCid") String newCid, @Param("cname") String cname);

    Category findCategoryById(String cid);

    int editCategoryById(@Param("cid") String cid,@Param("cname") String cname);

    List<Product> findProductByPage();

    int getProductRows();

    int addProduct(Product product);
}
