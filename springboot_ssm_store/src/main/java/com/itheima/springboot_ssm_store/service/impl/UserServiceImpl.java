package com.itheima.springboot_ssm_store.service.impl;

import com.itheima.springboot_ssm_store.dao.UserDao;
import com.itheima.springboot_ssm_store.domain.User;
import com.itheima.springboot_ssm_store.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public boolean regist(User user) {

        int a =  userDao.regist(user);
        System.out.println("注册返回值："+a);
        if(a >= 0){
            return  true;
        }
        return false;
    }

    @Override
    public User login(String username, String password) {


        return userDao.login(username,password);
    }
}
