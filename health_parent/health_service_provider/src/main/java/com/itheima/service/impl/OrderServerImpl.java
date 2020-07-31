package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.entity.Result;
import com.itheima.service.OrderService;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * 体检预约业务操作
 * @Author JackLiu
 * @Date 2020/7/31/12:10
 */

@Service(interfaceClass = OrderService.class)
@Transactional
public class OrderServerImpl implements OrderService {

    @Override
    public Result order(Map map) {
//        1.检查用户所选的日期是否已提交过预约
//        2.检查用户所选择的预约日期是否已经约满
//        3.检查是否重复预约
//        4.检查当前用户是否为会员,是直接完成预约,不是则自动注册并进行预约
//        5.预约成功 更新当日已预约的人数
        return null;
    }
}
