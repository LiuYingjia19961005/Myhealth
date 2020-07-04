package com.itheima.service;

import com.itheima.pojo.OrderSetting;

import java.util.List;
import java.util.Map;

/**
 * @Author JackLiu
 * @Date 2020/4/28/15:15
 */

public interface OrdersettingService {

    void add(List<OrderSetting> orderSettingList);

    List<Map> getOrderSettingByMonth(String date);
}
