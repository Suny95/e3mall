package com.e3mall.controller;

import com.e3mall.common.pojo.E3Result;
import com.e3mall.common.pojo.EasyUiTreeNode;
import com.e3mall.content.service.ContentCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by Suny on 2017/6/19.
 */
@Controller
public class ContentCategoryController {

    @Autowired
    private ContentCategoryService contentCategoryService;

    /**
     * 查询内容分类
     * @param parentId
     * @return
     */
    @RequestMapping("content/category/list")
    @ResponseBody
    public List<EasyUiTreeNode> queryContentCategoryByParentId(@RequestParam(value = "id",defaultValue = "0")Long parentId) {
        List<EasyUiTreeNode> list = contentCategoryService.queryContentCategoryByParentId(parentId);
        return list;
    }

    /**
     * 新增内容分类
     * @param parentId
     * @param name
     * @return
     */
    @RequestMapping("/content/category/create")
    @ResponseBody
    public E3Result addCategory(Long parentId,String name) {
        E3Result result = contentCategoryService.addCategory(parentId,name);
        return result;
    }

    /**
     * 根据id修改内容分类
     * @param id
     * @param name
     * @return
     */
    @RequestMapping("/content/category/update")
    @ResponseBody
    public E3Result updateCategory(Long id,String name) {
        E3Result result = contentCategoryService.updateCategory(id, name);
        return result;
    }

    /**
     * 根据id删除分类
     * @param id
     * @return
     */
    @RequestMapping("/content/category/delete/")
    @ResponseBody
    public E3Result delCategory(Long id) {
        E3Result result = contentCategoryService.delCategory(id);
        return result;
    }
}
