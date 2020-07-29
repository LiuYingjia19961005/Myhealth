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
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import redis.clients.jedis.JedisPool;

import java.io.*;
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

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    private String outputpath;

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

    //新增套餐同时关联检查组
    @Override
    public void add(Setmeal setmeal, Integer[] checkgroupIds) {
        this.setmealDao.add(setmeal);
        //设置套餐和 检查组的关联关系
        Integer id = setmeal.getId();
        setCheckGroupAndCheckItem(checkgroupIds, id);    //给套餐增加 检查组
        //将图片名称保存到Redis中
        String fileName = setmeal.getImg();
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES, fileName);

        //当添加套餐后需要重新生成静态页面(套餐列表页面 套餐详情页面)
        generateMobileStaticHtml();
    }

    //查询所有的套餐
    @Override
    public List<Setmeal> findAll() {
        return setmealDao.findAll();
    }

    //根据套餐ID查询套餐详情（套餐基本信息、套餐对应的检查组信息、检查组对应的检查项信息）
    @Override
    public Setmeal findById(int id) {
        System.out.println("SetmealServiceImpl---------" + id);
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

    /**
     * 生成当前方法所需的静态页面
     */
    public void generateMobileStaticHtml(){
        //生成静态页面之前需要查询所有的套餐列表数据数据
        List<Setmeal> setmealList = setmealDao.findAll();
        //需要生成套餐列表静态页面
        generateMobileSetmealListHtml(setmealList);
        //需要生成套餐详情静态页面(多个)
        generateMobileSetmealDetailHtml(setmealList);
    }

    /**
     * 需要生成套餐详情静态页面(可能有多个)
     */
    public void generateMobileSetmealDetailHtml(List<Setmeal> setmealList){
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("setmealList",setmealList);
        this.generateHtml("mobile_setmeal.ftl",
                "m_setmeal.html",
                dataMap);
    }

    /**
     * 生成套餐列表静态页面
     */
    public void generateMobileSetmealListHtml(List<Setmeal> setmealList){
        for (Setmeal setmeal:setmealList){
            Map<String,Object> dataMap = new HashMap<>();
            dataMap.put("setmeal",this.findById(setmeal.getId()));
            this.generateHtml("mobile_setmeal_detail.ftl",
                    "setmeal_detail_"+setmeal.getId()+".html",
                    dataMap);

        }
    }

    /**
     * 通用的方法，用于生成静态页面
     */
    public void generateHtml(String templateName, String htmlPageName, Map dataMap) {
        Configuration configuration = freeMarkerConfigurer.getConfiguration();
        Writer out = null;
        try {
            //加载模版文件
            Template template = configuration.getTemplate(templateName);
            // 生成数据
            File docFile = new File(outputpath + "\\" + htmlPageName);
            out = new BufferedWriter(new OutputStreamWriter(new
                    FileOutputStream(docFile)));
            // 输出文件
            template.process(dataMap, out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != out) {
                    out.flush();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }
}











