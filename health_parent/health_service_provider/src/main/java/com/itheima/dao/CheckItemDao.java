package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.CheckItem;

import java.util.List;

//服务接口
public interface CheckItemDao {
    void add(CheckItem checkItem);

    Page<CheckItem> selectByCondition(String queryString);

    void delete(Integer id);

    long findCountByCheckItemId(Integer id);    //根据检查项id查询是否还关联着检查组

    CheckItem findById(Integer id);

    void update(CheckItem checkItem);

    List<CheckItem> findAll();
}
