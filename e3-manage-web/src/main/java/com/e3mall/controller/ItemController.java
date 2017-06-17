package com.e3mall.controller;

import com.e3mall.common.pojo.EasyUiDatagrid;
import com.e3mall.pojo.TbItem;
import com.e3mall.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Suny on 2017/6/16.
 */
@Controller
public class ItemController {

    @Autowired
    private ItemService itemService;

    @RequestMapping("item/{itemId}")
    @ResponseBody
    public TbItem queryTbItemById(@PathVariable Long itemId){
        TbItem tbItem = itemService.queryTbItemById(itemId);
        return tbItem;
    }

    /**
     * 分页查询商品数据
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping(value = "item/list",method = RequestMethod.GET)
    @ResponseBody
    public EasyUiDatagrid queryTbItemPageList(Integer page,Integer rows) {
        EasyUiDatagrid datagrid = itemService.queryTbItemPageList(page, rows);
        return datagrid;
    }
}
