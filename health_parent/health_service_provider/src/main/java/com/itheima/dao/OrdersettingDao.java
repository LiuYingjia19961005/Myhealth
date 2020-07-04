package com.itheima.dao;

import com.itheima.pojo.OrderSetting;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author JackLiu
 * @Date 2020/4/28/19:40
 * 1:15
 */

public interface OrdersettingDao {
    void add(OrderSetting orderSetting);
    void editNumberByOrderDate(OrderSetting orderSetting);
    long findCountByOrderDate(Date orderDate);
    List<OrderSetting> getOrderSettingByMonth(Map map);
}
