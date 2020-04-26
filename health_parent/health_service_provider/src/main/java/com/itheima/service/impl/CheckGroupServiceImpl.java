package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.CheckGroupDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckGroup;
import com.itheima.service.CheckGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = CheckGroupService.class)
@Transactional
public class CheckGroupServiceImpl implements CheckGroupService {
    @Autowired
    private CheckGroupDao checkGroupDao;

    /**
     * 新增检查组的同时, 需要管理检查项
     * @param checkGroup
     * @param checkitemIds
     */
    @Override
    public void add(CheckGroup checkGroup, Integer[] checkitemIds) {
        //新增检查组
        this.checkGroupDao.add(checkGroup);
        //设置检查组和检查项的关联关系
        Integer id = checkGroup.getId();
        setCheckGroupAndCheckItem(checkitemIds, id);    //给检查组增加检查项
    }

    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();

        PageHelper.startPage(currentPage, pageSize);
        Page<CheckGroup> page = this.checkGroupDao.selectByCondition(queryString);

        long total = page.getTotal();
        List<CheckGroup> checkGroupList = page.getResult();

        return new PageResult(total,checkGroupList);
    }

    @Override
    public CheckGroup findById(Integer id) {
        return checkGroupDao.findById(id);
    }

    @Override
    public List<Integer> findCheckItemIdsByCheckGroupId(Integer id) {
        return this.checkGroupDao.findCheckItemIdsByCheckGroupId(id);
    }

    /**
     * 编辑需要关联 检查项
     * @param checkGroup
     * @param checkitemIds
     */
    @Override
    public void update(CheckGroup checkGroup, Integer[] checkitemIds) {
        this.checkGroupDao.update(checkGroup);  //修改检查组
        Integer id = checkGroup.getId();
        this.checkGroupDao.cleanCheckitem(id);  //清空关联的检查项
        setCheckGroupAndCheckItem(checkitemIds, id);    //更新检查项
    }

    @Override
    public void delete(Integer id) {
        this.checkGroupDao.deleteByCheckitem(id);
        this.checkGroupDao.delete(id);
    }

    @Override
    public List<CheckGroup> findAll() {
        return this.checkGroupDao.findAll();
    }

    /**
     * 抽取重复代码
     */
    public void setCheckGroupAndCheckItem(Integer[] checkitemIds,Integer id){
        if(checkitemIds.length > 0 && checkitemIds != null){
            Map<String,Integer> map = null;
            for (Integer checkitemId : checkitemIds) {
                map = new HashMap<>();
                map.put("checkgroupId",id);
                map.put("checkitemId",checkitemId);
                this.checkGroupDao.setCheckGroupAndCheckItem(map);  //更新检查项
            }
        }
    }
}
