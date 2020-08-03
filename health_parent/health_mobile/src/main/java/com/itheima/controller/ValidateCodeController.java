package com.itheima.controller;

import com.aliyuncs.exceptions.ClientException;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.utils.SMSUtils;
import com.itheima.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

/**
 * @Author JackLiu
 * @Date 2020/7/30/18:08
 * 短信验证码操作
 */

@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {
    @Autowired
    private JedisPool jedisPool;

    /**
     * 用户在线体检预约发送验证码
     */
    @RequestMapping("/send40rder")
    public Result send40rder(String telephone){
        //随机生成4位数字
        Integer validateCode4String = ValidateCodeUtils.generateValidateCode(4);
        //给用户发送验证码
        try {
            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,validateCode4String.toString());
        } catch (ClientException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
        //将验证码保存到redis(5分钟)
        jedisPool.getResource().setex(telephone + RedisMessageConstant.SENDTYPE_ORDER,30000,validateCode4String.toString());
        return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }

    /**
     * 用户手机快速登录发送验证码
     */
    @RequestMapping("/send4Login")
    public Result send4Login(String telephone){
        //随机生成6位数字
        Integer validateCode4String = ValidateCodeUtils.generateValidateCode(6);
        //给用户发送验证码
        try {
            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,validateCode4String.toString());
        } catch (ClientException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
        //将验证码保存到redis(5分钟)
        jedisPool.getResource().setex(telephone + RedisMessageConstant.SENDTYPE_LOGIN,30000,validateCode4String.toString());
        return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }
}
