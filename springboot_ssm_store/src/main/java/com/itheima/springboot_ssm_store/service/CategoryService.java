package com.itheima.springboot_ssm_store.service;

import com.itheima.springboot_ssm_store.domain.Category;

import java.util.List;

public interface CategoryService {

    List<Category> findAll();
}
