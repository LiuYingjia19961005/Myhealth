package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckItem;
import com.itheima.service.CheckItemService;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 检查项管理
 */

@RestController
@RequestMapping("checkitem")
public class CheckItemController {

    @Reference
    private CheckItemService checkItemService;

    /**
     * 新增检查项
     * @param checkItem
     * @return
     * @RequestBody 解析前台传过来的数据
     */
    @RequestMapping("/add")
    public Result add(@RequestBody CheckItem checkItem){
        try {
            checkItemService.add(checkItem);
        } catch (Exception e) { //服务调用失败
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_CHECKITEM_FAIL);
        }
        return new Result(true, MessageConstant.ADD_CHECKITEM_SUCCESS);
    }

    /**
     * 检查项分页
     * @param queryPageBean
     * @return
     * @RequestBody 解析前台传过来的数据
     */
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        return this.checkItemService.findPage(queryPageBean);
    }

    /**
     * 检查项删除
     * @param id
     * @return
     */
    @RequestMapping("/delete")
    public Result delete(@Param(value = "id")Integer id){
        try {
            this.checkItemService.delete(id);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.DELETE_CHECKITEM_FAIL);
        }
        return new Result(true, MessageConstant.DELETE_CHECKITEM_SUCCESS);
    }

    /**
     * 修改
     * @param checkItem
     * @return
     */
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public Result update(@RequestBody CheckItem checkItem){
        try {
            this.checkItemService.update(checkItem);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_CHECKITEM_FAIL);
        }
        return new Result(true, MessageConstant.EDIT_CHECKITEM_FAIL);
    }

    /**
     * 通过id查询一个检查项
     * @param
     * @return
     */
    @GetMapping("/findById")
    public Result findById(@Param("id") Integer id){
        try {
            CheckItem checkItem = this.checkItemService.findById(id);
            return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS,checkItem);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_CHECKITEM_FAIL);
        }
    }

    /**
     * 查询所有的检查项
     */
    @GetMapping("/findAll")
    public Result findAll(){
        try {
            List<CheckItem> checkItems = this.checkItemService.findAll();
            return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS,checkItems);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_CHECKITEM_FAIL);
        }
    }

}
