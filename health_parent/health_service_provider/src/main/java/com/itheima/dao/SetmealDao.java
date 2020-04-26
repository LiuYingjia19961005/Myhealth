package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.Setmeal;

import java.util.List;
import java.util.Map;

public interface SetmealDao {
    Page<Setmeal> selectByCondition(String queryString);

    void add(Setmeal setmeal);

    void setCheckGroupAndCheckItem(Map<String, Integer> map);
}
