package com.e3mall.service.impl;

import com.e3mall.mapper.TbItemMapper;
import com.e3mall.pojo.TbItem;
import com.e3mall.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Suny on 2017/6/16.
 */
@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private TbItemMapper itemMapper;

    /**
     * 根据ID查询商品
     * @param id
     * @return
     */
    @Override
    public TbItem queryTbItemById(Long id) {
        return itemMapper.selectByPrimaryKey(id);
    }
}
