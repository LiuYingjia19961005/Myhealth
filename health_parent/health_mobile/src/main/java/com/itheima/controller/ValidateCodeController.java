package com.itheima.controller;

import com.itheima.entity.Result;
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
@RequestMapping("validateCode")
public class ValidateCodeController {
    @Autowired
    private JedisPool jedisPool;

    /**
     * 用户在线预约发送验证码
     */
    @RequestMapping("send40rder")
    public Result send40rder(String telephone){
        //给用户发送验证码

        //将验证码保存到redis(5分钟)
        return null;
    }
}
