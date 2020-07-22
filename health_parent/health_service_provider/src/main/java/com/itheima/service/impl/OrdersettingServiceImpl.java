package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.OrdersettingDao;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrdersettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    /**
     * 根据月份查询对应点的预约设置数据
     * @param date
     * @return
     */
    @Override
    public List<Map> getOrderSettingByMonth(String date) {
        String startTime = date + "-1"; //yyyy-MM-1
        String endTime = date + "-31";  //yyyy-MM-31
        Map<String, String> map = new HashMap<>();
        map.put("startTime",startTime);
        map.put("endTime",endTime);
        //调用dao,根据日期范围查询相应的数据
        List<OrderSetting> list = ordersettingDao.getOrderSettingByMonth(map);
        for (OrderSetting orderSetting : list) {
            System.out.println(orderSetting);
        }
        List<Map> result = new ArrayList<>();
        if(null != list && list.size() > 0){
            for (OrderSetting orderSetting:list) {
                Map<String,Object> stringMap = new HashMap<>();
                stringMap.put("date",orderSetting.getOrderDate().getDate());    //获取日期几号
                stringMap.put("number",orderSetting.getNumber());
                stringMap.put("reservations",orderSetting.getReservations());
                result.add(stringMap);
            }
        }
        return result;
    }
}