package com.e3mall.service;

import com.e3mall.common.pojo.E3Result;
import com.e3mall.common.pojo.EasyUiDatagrid;
import com.e3mall.pojo.TbItem;

import java.util.List;

/**
 * Created by Suny on 2017/6/16.
 */
public interface ItemService {

    TbItem queryTbItemById(Long id);

    public EasyUiDatagrid queryTbItemPageList(Integer pageNum, Integer pageSize);

    public E3Result addItem(TbItem tbItem, String desc);

    public E3Result updateItemAndItemDesc(TbItem item, String desc);

    public E3Result deleteItemAndItemDesc(List<Long> ids);

    public E3Result instockItem(List<Long> ids);

    public E3Result reshelfItem(List<Long> ids);
}
