package com.e3mall.item.controller;

import com.e3mall.item.pojo.Item;
import com.e3mall.pojo.TbItem;
import com.e3mall.pojo.TbItemDesc;
import com.e3mall.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 商品详情Controller
 * Created by Suny on 2017/6/22.
 */
@Controller
public class ItemController {
    @Autowired
    private ItemService itemService;

    @RequestMapping("item/{itemId}")
    public String showItemInfo(@PathVariable Long itemId, Model model) {
        //查询商品信息
        TbItem tbItem = itemService.queryTbItemById(itemId);
        //重新封装pojo,里面有页面需要的字段
        Item item = new Item(tbItem);
        //查询商品描述信息
        TbItemDesc itemDesc = itemService.queryItemDescById(itemId);
        //传递信息到页面
        model.addAttribute("item",item);
        model.addAttribute("itemDesc",itemDesc);
        //返回逻辑视图
        return "item";
    }
}
