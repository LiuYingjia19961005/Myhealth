package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.CheckGroup;

import java.util.List;
import java.util.Map;

public interface CheckGroupDao {
    void add(CheckGroup checkGroup);

    void setCheckGroupAndCheckItem(Map<String, Integer> map);

    Page<CheckGroup> selectByCondition(String queryString);

    CheckGroup findById(Integer id);

    List<Integer> findCheckItemIdsByCheckGroupId(Integer id);

    void update(CheckGroup checkGroup);

    void cleanCheckitem(Integer id);

    void delete(Integer id);

    void deleteByCheckitem(Integer id);

    List<CheckGroup> findAll();
}
