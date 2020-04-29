package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.OrdersettingDao;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrdersettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author JackLiu
 * @Date 2020/4/28/19:30
 * 预约设置服务
 */
@Service(interfaceClass = OrdersettingService.class)
@Transactional
public class OrdersettingServiceImpl implements OrdersettingService {

    @Autowired
    private OrdersettingDao ordersettingDao;

    //批量导入预约设置数据
    @Override
    public void add(List<OrderSetting> orderSettingList) {
        if(null != orderSettingList && orderSettingList.size() != 0){
            //已经进行了预约设置, 执行更新操作
            for (OrderSetting orderSetting : orderSettingList) {
                long countByOrderDate = this.ordersettingDao.findCountByOrderDate(orderSetting.getOrderDate());
                if(countByOrderDate>0){
                    //已经进行了预约设置, 执行更新操作
                    this.ordersettingDao.editNumberByOrderDate(orderSetting);
                }else {
                    //没有进行预约设置, 执行插入操作
                    this.ordersettingDao.add(orderSetting);
                }
            }
        }
    }
}
