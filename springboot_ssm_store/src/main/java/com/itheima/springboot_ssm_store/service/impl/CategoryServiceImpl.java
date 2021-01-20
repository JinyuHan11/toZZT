package com.itheima.springboot_ssm_store.service.impl;

import com.itheima.springboot_ssm_store.dao.CategoryDao;
import com.itheima.springboot_ssm_store.domain.Category;
import com.itheima.springboot_ssm_store.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryDao categoryDao;
    @Override
    public List<Category> findAll() {

        return categoryDao.findAll();
    }
}
