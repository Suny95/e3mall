package com.e3mall.service.impl;

import com.e3mall.common.pojo.E3Result;
import com.e3mall.common.pojo.EasyUiDatagrid;
import com.e3mall.common.utils.IDUtils;
import com.e3mall.mapper.TbItemDescMapper;
import com.e3mall.mapper.TbItemMapper;
import com.e3mall.pojo.TbItem;
import com.e3mall.pojo.TbItemDesc;
import com.e3mall.pojo.TbItemDescExample;
import com.e3mall.pojo.TbItemExample;
import com.e3mall.service.ItemService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by Suny on 2017/6/16.
 */
@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private TbItemMapper itemMapper;
    @Autowired
    private TbItemDescMapper itemDescMapper;

    /**
     * 根据ID查询商品
     * @param id
     * @return
     */
    @Override
    public TbItem queryTbItemById(Long id) {
        return itemMapper.selectByPrimaryKey(id);
    }

    /**
     * 分页查询商品表
     * @param pageNum
     * @param pageSize
     * @return
     */
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

    /**
     * 新增商品表和商品描述表
     * @param tbItem
     * @param desc
     * @return
     */
    @Override
    public E3Result addItem(TbItem tbItem, String desc) {
        //补全信息
        long itemId = IDUtils.genItemId();
        tbItem.setId(itemId);
        //1-正常，2-下架，3-删除
        tbItem.setStatus((byte) 1);
        tbItem.setCreated(new Date());
        tbItem.setUpdated(tbItem.getCreated());
        //插入数据
        itemMapper.insertSelective(tbItem);
        //补全商品描述表
        TbItemDesc itemDesc = new TbItemDesc();
        itemDesc.setItemId(itemId);
        itemDesc.setItemDesc(desc);
        itemDesc.setCreated(tbItem.getCreated());
        itemDesc.setUpdated(tbItem.getCreated());
        //插入数据
        itemDescMapper.insertSelective(itemDesc);
        //返回数据
        E3Result result = new E3Result(200, "OK", null);
        return result;
    }

    /**
     * 编辑商品对象和商品描述对象
     * @param tbItem
     * @param desc
     * @return
     */
    public E3Result updateItemAndItemDesc(TbItem tbItem, String desc) {
        //修改时间
        tbItem.setUpdated(new Date());
        itemMapper.updateByPrimaryKeySelective(tbItem);

        //修改商品描述表
        TbItemDesc itemDesc = new TbItemDesc();
        itemDesc.setUpdated(tbItem.getUpdated());
        itemDesc.setItemId(tbItem.getId());
        itemDesc.setItemDesc(desc);
        //执行修改
        itemDescMapper.updateByPrimaryKeySelective(itemDesc);
        //返回结果
        E3Result result = new E3Result(200, "OK", null);
        return result;
    }

    /**
     * 根据多个id删除商品对象和商品描述对象
     * @param ids
     * @return
     */
    @Override
    public E3Result deleteItemAndItemDesc(List<Long> ids) {
        TbItemExample itemExample = new TbItemExample();
        //设置条件
        itemExample.createCriteria().andIdIn(ids);
        itemMapper.deleteByExample(itemExample);

        TbItemDescExample itemDescExample = new TbItemDescExample();
        //设置条件
        itemDescExample.createCriteria().andItemIdIn(ids);
        itemDescMapper.deleteByExample(itemDescExample);

        E3Result result = new E3Result(200, "OK", null);
        return result;
    }

    /**
     * 根据条件批量下架商品
     * @param ids
     * @return
     */
    @Override
    public E3Result instockItem(List<Long> ids) {
        TbItemExample itemExample = new TbItemExample();
        //设置条件
        itemExample.createCriteria().andIdIn(ids);
        TbItem tbItem = new TbItem();
        // 1-正常 2-下架 3-删除
        tbItem.setStatus((byte) 2);
        itemMapper.updateByExampleSelective(tbItem,itemExample);

        E3Result result = new E3Result(200, "OK", null);
        return result;
    }

    /**
     * 根据条件批量上架商品
     * @param ids
     * @return
     */
    @Override
    public E3Result reshelfItem(List<Long> ids) {
        TbItemExample itemExample = new TbItemExample();
        //设置条件
        itemExample.createCriteria().andIdIn(ids);
        TbItem tbItem = new TbItem();
        // 1-正常 2-下架 3-删除
        tbItem.setStatus((byte) 1);
        itemMapper.updateByExampleSelective(tbItem,itemExample);

        E3Result result = new E3Result(200, "OK", null);
        return result;
    }
}
