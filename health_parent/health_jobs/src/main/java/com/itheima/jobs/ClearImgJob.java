package com.itheima.jobs;

//切换窗口 ctrl + tab

import com.itheima.constant.RedisConstant;
import com.itheima.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisPool;

import java.util.Set;

/**
 * 定时清理垃圾图片
 */
public class ClearImgJob {

    @Autowired
    private JedisPool jedisPool;

    public void clearImg(){
        //根据Redis中保存的两个set集合进行差值计算, 并获得垃圾图片集合名称
        Set<String> sdiff = jedisPool.getResource().sdiff(RedisConstant.SETMEAL_PIC_RESOURCES, RedisConstant.SETMEAL_PIC_DB_RESOURCES);
        for (String picName : sdiff) {
            //删除七牛云服务器上的图片
            QiniuUtils.deleteFileFromQiniu(picName);
            //从redis集合中删除图片名称
            jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_RESOURCES,picName);
            System.out.println("清理垃圾图片成功====" + picName);
        }
    }

    //待完成, 每隔一个月清除一次大小集合, 要先删除一次垃圾图片, 在执行清空
    public void clearMothImg(){
        this.clearImg();
        jedisPool.getResource().del(RedisConstant.SETMEAL_PIC_DB_RESOURCES);
        jedisPool.getResource().del(RedisConstant.SETMEAL_PIC_RESOURCES);
    }
}
