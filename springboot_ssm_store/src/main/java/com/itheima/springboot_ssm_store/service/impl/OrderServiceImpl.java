package com.itheima.springboot_ssm_store.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.springboot_ssm_store.common.OrderConst;
import com.itheima.springboot_ssm_store.dao.OrderDao;
import com.itheima.springboot_ssm_store.domain.Order;
import com.itheima.springboot_ssm_store.domain.OrderItem;
import com.itheima.springboot_ssm_store.domain.OrderItemView;
import com.itheima.springboot_ssm_store.domain.PageBean;
import com.itheima.springboot_ssm_store.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {


    @Autowired
    private OrderDao orderDao;
    @Override
    public boolean saveOrderAndOrderItem(Order order, List<OrderItem> orderItemsList) {
        //发两个sql 一个insert into order，一个insert into orderItem
        try{
            orderDao.saveOrder(order);
            for (OrderItem orderItem:
                 orderItemsList) {
                orderDao.saveOrderItem(orderItem);
            }

        }catch(Exception e){
        e.printStackTrace();
        return false;
    }
        return false;
    }

    @Override
    public PageBean getOrdersListByPage(String uid, String currentPage) {
        PageBean pageBean = new PageBean();

        //未完成
        Page<Order> orderPage = PageHelper.startPage(Integer.parseInt(currentPage),OrderConst.PAGE_SIZE);
        List<Order> orderList = orderDao.getOrdersListByPage(uid);

        for (Order order:
             orderList) {
            //从数据库中查询出订单视图对象集合
            List<OrderItemView> orderVIewList = orderDao.getOrderViewList(order.getOid());
            order.setOrderViewList(orderVIewList);

        }
        pageBean.setCurrentPage(Integer.parseInt(currentPage));
        pageBean.setPageSize(OrderConst.PAGE_SIZE);
        pageBean.setTotalCount(orderPage.getTotal());
        pageBean.setTotalPage(orderPage.getPages());
        pageBean.setList(orderList);
        return pageBean;


    }

    //少内容
    @Override
    public Order getOrdersById(String oid) {
        Order order = orderDao.getOrdersById(oid);
        order.setOrderViewList(orderDao.getOrderViewList(order.getOid()));
        return order;
    }

    @Override
    public boolean updateOrdersStateById(String oid, Integer state) {
        int a=orderDao.updateOrdersStateById(oid,state);
        if(a>=0){
            return true;
        }

        return false;
    }

    @Override
    public boolean updateOrderById(Order order) {
        int a = orderDao.updateOrderById(order);
        if(a>0){
            return true;
        }

        return false;
    }
}
