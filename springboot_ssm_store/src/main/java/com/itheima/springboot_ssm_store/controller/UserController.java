package com.itheima.springboot_ssm_store.controller;

import com.itheima.springboot_ssm_store.domain.User;
import com.itheima.springboot_ssm_store.service.UserService;
import com.itheima.springboot_ssm_store.utils.Result;
import com.itheima.springboot_ssm_store.utils.UUIDUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@RestController   //@Controller+@ResponseBody
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/user")
    public Result userMethod(String method, User user, HttpServletResponse response,HttpServletRequest request) throws UnsupportedEncodingException {


        //不论登录或者注册 都会走这个方法
        //1.根据 method的值 来判断登录还是注册
        //注册
        if("register".equals(method)){
            System.out.println("进入到注册方法！");
            // 创建主键
            user.setUid(UUIDUtils.getUUID());
            //调用service注册
            if(userService.regist(user)){
                //注册成功时：
                return  new Result(Result.SUCCESS,"注册成功！");
            }
            //注册失败时：
            return new Result(Result.FAILS,"注册失败！");
        }else if("login".equals(method)){
            //登录时：
            /*获取前端输入的用户名和密码*/
            String username = user.getUsername();
            String password = user.getPassword();
            //调用业务层 去实现登录
            User userFromData = userService.login(username,password);
            if(userFromData!=null){
                //登录成功
                //把整个user对象存入session
                request.getSession().setAttribute("user",userFromData);
                //1.把用户名存入cookie ，方便前端去展示用户名
                Cookie cookie = new Cookie("username", URLEncoder.encode(username,"utf-8"));
                cookie.setMaxAge(60 * 10);
                cookie.setPath("/");
                cookie.setDomain("itheimashop.com");
                response.addCookie(cookie);
                //2.返回结果信息  code ：1
                return  new Result(Result.SUCCESS,"登录成功！");
            }
            //登录失败
            return  new Result(Result.FAILS,"登录失败！");
        }else if("loginOut".equals(method)){
            //登出时： 清除cookie
            // 删除Cookie
            Cookie cookie = new Cookie("username", null);
            cookie.setMaxAge(0);
            cookie.setDomain("itheimashop.com");
            response.addCookie(cookie);
            return  new Result(Result.SUCCESS,"登出成功！");
        }

        return  null;
    }

}
