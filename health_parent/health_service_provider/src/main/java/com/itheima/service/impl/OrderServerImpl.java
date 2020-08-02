package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.constant.MessageConstant;
import com.itheima.dao.MemberDao;
import com.itheima.dao.OrderDao;
import com.itheima.dao.OrdersettingDao;
import com.itheima.entity.Result;
import com.itheima.pojo.Member;
import com.itheima.pojo.Order;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrderService;
import com.itheima.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 体检预约业务操作
 * @Author JackLiu
 * @Date 2020/7/31/12:10
 */

@Service(interfaceClass = OrderService.class)
@Transactional
public class OrderServerImpl implements OrderService {
    @Autowired
    private MemberDao memberDao;

    @Autowired
    private OrdersettingDao ordersettingDao;

    @Autowired
    private OrderDao orderDao;

    @Override
    public Result order(Map map) {
//      1.检查用户所选的日期查询是否已提交过预约
        String orderDate = (String) map.get("orderDate");
        OrderSetting orderSetting = ordersettingDao.findByOrderDate(orderDate);
        System.out.println("OrderServerImpl====" + orderSetting);
        String setmealId = null;
        if(orderSetting == null){
            //指定日期没有进行预约设置，无法完成体检预约
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }
//      2.检查用户所选择的预约日期是否已经约满
        if(orderSetting.getReservations() >= orderSetting.getNumber()) {
            return new Result(false, MessageConstant.ORDER_FULL);
        }
//      3.判断用户是否在重复预约--------------------
        String telephone = (String) map.get("telephone");

        List<Member> memberList = memberDao.findAll();
        System.out.println("OrderServerImpl====memberList====" + memberList);

        Member member = memberDao.findByTelephone(telephone);
        System.out.println("OrderServerImpl====member====" + member);

        if(member != null){
            Integer memberId = member.getId();  //会员id
            try {
                Date order_Date = DateUtils.parseString2Date(orderDate);    //预约日期
                setmealId = (String) map.get("setmealId");   //套餐id
                Order order = new Order(memberId, order_Date, Integer.parseInt(setmealId));
                //根据条件进行查询
                List<Order> orderList = orderDao.findByCodition(order);
                if(null != orderList || orderList.size() >0){
                    //说明用户在重复预约,无法完成再次预约
                    return new Result(false,MessageConstant.HAS_ORDERED);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
//          4.检查当前用户是否为会员,是直接完成预约,不是则自动注册并进行预约
            member = new Member();
            member.setPhoneNumber(telephone);
            member.setName((String)map.get("name"));
            member.setIdCard((String)map.get("idCard"));
            member.setSex((String)map.get("sex"));
            member.setRegTime(new Date());
            memberDao.add(member); //自动完成会员注册
        }
//      5.预约成功 更新当日已预约的人数
        Order order = new Order();
        order.setId(member.getId());    //设置会员id
        try {
            order.setOrderDate(DateUtils.parseString2Date(orderDate));
        } catch (Exception e) {
            e.printStackTrace();
        }
        order.setOrderType((String)map.get("orderType"));//预约类型
        order.setOrderStatus(Order.ORDERSTATUS_NO); //到诊状态
        order.setSetmealId(Integer.parseInt(setmealId));    //设置套餐id
        try {
            order.setOrderDate(DateUtils.parseString2Date(orderDate));
        } catch (Exception e) {
            e.printStackTrace();
        }
        orderDao.add(order);    //预约成功,添加记录
        orderSetting.setReservations(orderSetting.getReservations() + 1);
        ordersettingDao.updateReservations(orderSetting);   //更新已预约人数
        return new Result(true, MessageConstant.ORDERSETTING_SUCCESS,order.getId());
    }
}
