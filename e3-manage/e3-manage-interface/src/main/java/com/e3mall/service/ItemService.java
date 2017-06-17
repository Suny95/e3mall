package com.e3mall.service;

import com.e3mall.common.pojo.EasyUiDatagrid;
import com.e3mall.pojo.TbItem;

/**
 * Created by Suny on 2017/6/16.
 */
public interface ItemService {

    TbItem queryTbItemById(Long id);

    public EasyUiDatagrid queryTbItemPageList(Integer pageNum, Integer pageSize);
}
