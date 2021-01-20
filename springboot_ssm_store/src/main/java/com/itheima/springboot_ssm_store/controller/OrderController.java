package com.itheima.springboot_ssm_store.controller;

import com.itheima.springboot_ssm_store.alipay.AlipayWebPay;
import com.itheima.springboot_ssm_store.common.OrderConst;
import com.itheima.springboot_ssm_store.domain.*;
import com.itheima.springboot_ssm_store.service.OrderService;
import com.itheima.springboot_ssm_store.utils.Result;
import com.itheima.springboot_ssm_store.utils.UUIDUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@RestController
public class OrderController {

    @Autowired
    OrderService orderService;

    @RequestMapping("/order")
    public Result order(String method, HttpServletRequest request,String currentPage,String oid,
                        Order order1) throws Exception {
        //验证用户是否登录
        User user = (User)request.getSession().getAttribute("user");
        if(user==null){
            return new Result(Result.NOLOGIN,"尚未登录，请先登录");

        }
        //验证购物车是否为空
        Cart cart = (Cart)request.getSession().getAttribute("cart");
        if(cart == null){
            return new Result(Result.FAILS,"购物车为空，不可提交订单");

        }


        if("submitOrder".equals(method)){
            //提交订单-->保存订单信息-->包括两个表，order和orderItem
            //创建Order对象
            Order order2 = new Order();
            //保存订单id
            order2.setOid(UUIDUtils.getUUID());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            order2.setOrdertime(sdf.format(new Date()));
            order2.setTotal(cart.getTotal());
            order2.setState(OrderConst.UN_PAID);
            order2.setUid(user.getUid());

            //把购物项数据转换成订单项
            Collection<CartItem> cartItems = cart.getCartItemMap();
            List<OrderItem> orderItemsList = new ArrayList();

            for(CartItem cartItem:cartItems){
                //把遍历出来的购物项转换为订单项
                OrderItem orderItem = new OrderItem();
                orderItem.setSubTotal(cartItem.getSubTotal());
                orderItem.setCount(cartItem.getCount());
                orderItem.setPid(cartItem.getProduct().getPid());
                orderItem.setOid(order2.getOid());
                //把没一个转换完成的订单项保存在订单项集合中
                orderItemsList.add(orderItem);
            }

            if(orderService.saveOrderAndOrderItem(order2,orderItemsList)){
                //如果保存成功
                //清空购物车，避免多次订单重复提交
                cart.clearCart();
                return new Result(Result.SUCCESS,"保存订单成功");

            }else{
                return new Result(Result.FAILS,"保存订单失败");
            }

        }else if("getOrdersListByPage".equals(method)){
            //展示订单列表（带分页）
            PageBean pageBean= orderService.getOrdersListByPage(user.getUid(),currentPage);
            return new Result(Result.SUCCESS,"查询订单列表成功",pageBean);
        }else if("getOrdersById".equals(method)){
            Order order3 = orderService.getOrdersById(oid);
            if(order3.getState() == OrderConst.UN_PAID) {
                AlipayWebPay.queryTrade(order3.getOid(), "");
            }
            return new Result(1,null,order3);
        }else if("pay".equals(method)){
            if(orderService.updateOrderById(order1)){
                Order order2=orderService.getOrdersById(order1.getOid());
                String payResult = AlipayWebPay.getWebPayBody(order2);
                //  响应前端
                return new Result(Result.SUCCESS,"保存订单成功",payResult);
            }

        }


            return null;
    }

}
