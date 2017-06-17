package com.e3mall.service.impl;

import com.e3mall.common.pojo.EasyUiDatagrid;
import com.e3mall.mapper.TbItemMapper;
import com.e3mall.pojo.TbItem;
import com.e3mall.pojo.TbItemExample;
import com.e3mall.service.ItemService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public EasyUiDatagrid queryTbItemPageList(Integer pageNum, Integer pageSize) {
        //开启分页助手
        PageHelper.startPage(pageNum,pageSize);
        //设置条件对象,没有条件代表查询全部
        TbItemExample tbItemExample = new TbItemExample();
        //查询商品
        List<TbItem> items = itemMapper.selectByExample(tbItemExample);
        //将结果进行包装
        PageInfo<TbItem> pageInfo = new PageInfo<TbItem>(items);
        //创建返回值对象
        EasyUiDatagrid datagrid = new EasyUiDatagrid();
        //设置总条数
        datagrid.setTotal(pageInfo.getTotal());
        //设置分页的结果
        datagrid.setRows(pageInfo.getList());
        return datagrid;
    }
}
