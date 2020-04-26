package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.CheckItemDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckItem;
import com.itheima.service.CheckItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 检查项服务
 */
@Service(interfaceClass = CheckItemService.class)
@Transactional
public class CheckItemServiceImpl implements CheckItemService {

    @Autowired
    private CheckItemDao checkItemDao;

    @Override
    public void add(CheckItem checkItem) {
        checkItemDao.add(checkItem);
    }

    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();

        PageHelper.startPage(currentPage, pageSize);
        Page<CheckItem> page = this.checkItemDao.selectByCondition(queryString);

        long total = page.getTotal();
        List<CheckItem> rows = page.getResult();

        return new PageResult(total,rows);
    }

    @Override
    public void delete(Integer id) {
        //如果和检查组还有关联, 则不能删除
        long count = this.checkItemDao.findCountByCheckItemId(id);
        if (count > 0){
            throw new RuntimeException("当前检查项已关联到其他检查组,不能删除");
        }else {
            this.checkItemDao.delete(id);
        }
    }

    @Override
    public CheckItem findById(Integer id) {
        return this.checkItemDao.findById(id);
    }

    @Override
    public void update(CheckItem checkItem) {
        this.checkItemDao.update(checkItem);
    }

    @Override
    public List<CheckItem> findAll() {
        return this.checkItemDao.findAll();
    }
}
