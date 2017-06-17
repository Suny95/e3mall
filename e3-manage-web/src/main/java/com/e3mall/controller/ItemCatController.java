package com.e3mall.controller;

import com.e3mall.common.pojo.EasyUiTreeNode;
import com.e3mall.service.ItemCatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by Suny on 2017/6/17.
 */
@Controller
public class ItemCatController {

    @Autowired
    private ItemCatService itemCatService;

    @RequestMapping("item/cat/list")
    @ResponseBody                                   //由于第一次查询没有传id过来,所以设置一个默认值,直接查询顶级类目
    public List<EasyUiTreeNode> queryItemCatList(@RequestParam(value = "id",defaultValue = "0") Long parentId) {
        List<EasyUiTreeNode> nodeList = itemCatService.queryItemCatList(parentId);
        return nodeList;
    }
}
