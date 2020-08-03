package com.itheima.dao;

import com.itheima.pojo.Order;

import java.util.List;
import java.util.Map;

public interface OrderDao {

    List<Order> findByCodition(Order order);

    void add(Order order);

    Map findById4Detail(Integer id);
}
