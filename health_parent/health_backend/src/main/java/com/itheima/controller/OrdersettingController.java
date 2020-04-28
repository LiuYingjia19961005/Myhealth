package com.itheima.controller;

import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrdersettingService;
import com.itheima.utils.POIUtils;
import jdk.nashorn.internal.ir.annotations.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author JackLiu
 * @Date 2020/4/28/15:07
 */
@RestController
@RequestMapping("ordersetting")
public class OrdersettingController {

    @Reference
    private OrdersettingService ordersettingService;

    @RequestMapping("upload")
    public Result upload(@RequestParam("excelFile") MultipartFile excelFile){
        try {
            List<String[]> readExcel = POIUtils.readExcel(excelFile);
            List<OrderSetting> orderSettingList = new ArrayList<>();
            OrderSetting orderSetting;
            for (String[] strings : readExcel) {
                String orderDate = strings[0];
                String number = strings[1];
                orderSetting = new OrderSetting(new Date(orderDate), Integer.parseInt(number));
                orderSettingList.add(orderSetting);
            }
            //通过dubbo远程调用服务实现数据批量导入到数据库
            this.ordersettingService.add(orderSettingList);
            return new Result(true, MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.IMPORT_ORDERSETTING_FAIL);
        }
        return null;
    }

}
