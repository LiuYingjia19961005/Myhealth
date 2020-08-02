package com.itheima.dao;

import com.itheima.pojo.Member;

import java.util.List;

/**
 * @Author JackLiu
 * @Date 2020/7/31/16:01
 */


public interface MemberDao {

    Member findByTelephone(String telephone);

    void add(Member member);

    List<Member> findAll();
}
