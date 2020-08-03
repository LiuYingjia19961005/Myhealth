package com.itheima.service;

import com.itheima.entity.Result;
import com.itheima.pojo.Order;

import java.util.Map;

/**
 * @Author JackLiu
 * @Date 2020/7/31/10:13
 */

public interface OrderService {
    Result order(Map map);

    Map findById(Integer id);
}
