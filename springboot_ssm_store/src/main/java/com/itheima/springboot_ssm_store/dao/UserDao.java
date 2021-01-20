package com.itheima.springboot_ssm_store.dao;

import com.itheima.springboot_ssm_store.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserDao {

    int regist(User user);

    User login(@Param("username") String username, @Param("password") String password);
}
