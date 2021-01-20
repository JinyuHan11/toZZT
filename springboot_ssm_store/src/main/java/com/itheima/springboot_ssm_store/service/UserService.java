package com.itheima.springboot_ssm_store.service;

import com.itheima.springboot_ssm_store.domain.User;

public interface UserService {

    boolean regist(User user);
    User login(String username, String password);
}
