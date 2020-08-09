package com.itheima.dao;

import com.itheima.pojo.User;

import java.util.Map;

/**
 * @Author JackLiu
 * @Date 2020/8/6/22:45
 */

public interface UserDao {
    User findByUsernaem(String username);
}
