package com.itheima.springboot_ssm_store.dao;

import com.itheima.springboot_ssm_store.domain.Order;
import com.itheima.springboot_ssm_store.domain.OrderItem;
import com.itheima.springboot_ssm_store.domain.OrderItemView;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderDao {
    void saveOrder(Order order);

    void saveOrderItem(OrderItem orderItem);

    List<Order> getOrdersListByPage(String uid);

    List<OrderItemView> getOrderViewList(String oid);

    Order getOrdersById(String oid);

    void updateOrders(String oid,Order order);

    public int updateOrdersStateById(String oid,Integer state);

    int updateOrderById(Order order);
}
