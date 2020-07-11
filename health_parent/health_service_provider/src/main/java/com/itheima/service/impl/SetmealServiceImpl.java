package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.constant.RedisConstant;
import com.itheima.dao.SetmealDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.Setmeal;
import com.itheima.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = SetmealService.class)
@Transactional
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private JedisPool jedisPool;

    @Autowired
    private SetmealDao setmealDao;

    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();

        PageHelper.startPage(currentPage, pageSize);
        Page<Setmeal> page = this.setmealDao.selectByCondition(queryString);

        long total = page.getTotal();
        List<Setmeal> SetmealList = page.getResult();

        return new PageResult(total,SetmealList);
    }

    @Override
    public void add(Setmeal setmeal, Integer[] checkgroupIds) {
        this.setmealDao.add(setmeal);
        //设置套餐和 检查组的关联关系
        Integer id = setmeal.getId();
        setCheckGroupAndCheckItem(checkgroupIds, id);    //给套餐增加 检查组
        //将图片名称保存到Redis中
        String fileName = setmeal.getImg();
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES, fileName);
    }

    //查询所有的套餐
    @Override
    public List<Setmeal> findAll() {
        return setmealDao.findAll();
    }

    //根据套餐ID查询套餐详情（套餐基本信息、套餐对应的检查组信息、检查组对应的检查项信息）
    @Override
    public Setmeal findById(int id) {
        return setmealDao.findById(id);
    }

    /**
     * 抽取重复代码
     */
    public void setCheckGroupAndCheckItem(Integer[] checkitemIds,Integer id){
        if(checkitemIds.length > 0 && checkitemIds != null){
            Map<String,Integer> map = null;
            for (Integer checkitemId : checkitemIds) {
                map = new HashMap<>();
                map.put("setmealId",id);
                map.put("checkgroupId",checkitemId);
                this.setmealDao.setCheckGroupAndCheckItem(map);  //更新套餐
            }
        }
    }
}
