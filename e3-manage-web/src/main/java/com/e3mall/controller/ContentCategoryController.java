package com.e3mall.controller;

import com.e3mall.common.pojo.EasyUiTreeNode;
import com.e3mall.content.service.ContentCategoryService;
import com.e3mall.pojo.TbContentCategory;
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
    private ContentCategoryService categoryService;

    @RequestMapping("content/category/list")
    @ResponseBody
    public List<EasyUiTreeNode> queryContentCategoryByParentId(@RequestParam(value = "id",defaultValue = "0")Long parentId) {
        List<EasyUiTreeNode> list = categoryService.queryContentCategoryByParentId(parentId);
        return list;
    }
}
