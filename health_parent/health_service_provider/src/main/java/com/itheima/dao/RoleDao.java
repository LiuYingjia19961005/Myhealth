package com.itheima.dao;

import com.itheima.pojo.Role;

import java.util.Set;

/**
 * @Author JackLiu
 * @Date 2020/8/6/22:49
 */

public interface RoleDao {
    Set<Role> findByUserId(Integer userId);
}
