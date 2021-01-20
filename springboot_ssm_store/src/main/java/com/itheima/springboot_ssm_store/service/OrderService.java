package com.itheima.springboot_ssm_store.service;

import com.itheima.springboot_ssm_store.domain.Order;
import com.itheima.springboot_ssm_store.domain.OrderItem;
import com.itheima.springboot_ssm_store.domain.PageBean;

import java.util.List;

public interface OrderService {
    boolean saveOrderAndOrderItem(Order order, List<OrderItem> orderItemsList);

    PageBean getOrdersListByPage(String uid, String currentPage);

    Order getOrdersById(String oid);
    //void updateOrders(String oid,Order order);

    public boolean updateOrdersStateById(String oid,Integer state);

    boolean updateOrderById(Order order1);
}
