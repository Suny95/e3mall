package com.e3mall.controller;

import com.e3mall.common.pojo.E3Result;
import com.e3mall.common.pojo.EasyUiDatagrid;
import com.e3mall.pojo.TbItem;
import com.e3mall.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    /**
     * 新增商品对象和商品描述表
     * @param item
     * @param desc
     * @return
     */
    @RequestMapping(value = "item/save",method = RequestMethod.POST)
    @ResponseBody
    public E3Result addItemAndItemDesc(TbItem item, String desc) {
        E3Result result = itemService.addItem(item, desc);
        return result;
    }

    /**
     * 编辑商品对象和商品描述对象
     * @param item
     * @param desc
     * @return
     */
    @RequestMapping(value = "/rest/item/update",method = RequestMethod.POST)
    @ResponseBody
    public E3Result updateItemAndItemDesc(TbItem item,String desc) {
        E3Result result = itemService.updateItemAndItemDesc(item, desc);
        return result;
    }

    /**
     * 根据多个id删除多个商品对象和商品描述对象
     * @param ids
     * @return
     */
    @RequestMapping(value = "/rest/item/delete",method = RequestMethod.POST)
    @ResponseBody
    public E3Result deleteItemAndItemDesc(@RequestParam("ids") List<Long> ids) {
        E3Result result = itemService.deleteItemAndItemDesc(ids);
        return result;
    }

    /**
     * 批量下架商品
     * @param ids
     * @return
     */
    @RequestMapping(value = "/rest/item/instock",method = RequestMethod.POST)
    @ResponseBody
    public E3Result instockItem(@RequestParam("ids") List<Long> ids) {
        E3Result result = itemService.instockItem(ids);
        return result;
    }

    /**
     * 批量上架商品
     * @param ids
     * @return
     */
    @RequestMapping(value = "/rest/item/reshelf",method = RequestMethod.POST)
    @ResponseBody
    public E3Result reshelfItem(@RequestParam("ids") List<Long> ids) {
        E3Result result = itemService.reshelfItem(ids);
        return result;
    }
}
