package com.itheima.springboot_ssm_store.controller;

import com.itheima.springboot_ssm_store.domain.Category;
import com.itheima.springboot_ssm_store.service.CategoryService;
import com.itheima.springboot_ssm_store.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @RequestMapping("/category")
    public Result getCategory(String method){
        //调用业务层，查询所有的分类
        List<Category> categoryList = categoryService.findAll();
        Result result = new Result(Result.SUCCESS, "查询分类成功", categoryList);
        return result;
    }

}
