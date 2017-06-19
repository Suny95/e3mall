package com.e3mall.content.service.impl;

import com.e3mall.common.pojo.E3Result;
import com.e3mall.common.pojo.EasyUiDatagrid;
import com.e3mall.content.service.ContentService;
import com.e3mall.mapper.TbContentMapper;
import com.e3mall.pojo.TbContent;
import com.e3mall.pojo.TbContentExample;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by Suny on 2017/6/19.
 */
@Service
public class ContentServiceImpl implements ContentService {

    @Autowired
    private TbContentMapper contentMapper;

    @Override
    public E3Result addContent(TbContent content) {
        content.setCreated(new Date());
        content.setUpdated(content.getCreated());
        contentMapper.insertSelective(content);

        E3Result result = new E3Result(200, "OK", null);
        return result;
    }

    @Override
    public EasyUiDatagrid queryContentById(Long categoryId, Integer page, Integer rows) {
        TbContentExample example = new TbContentExample();
        example.createCriteria().andCategoryIdEqualTo(categoryId);

        PageHelper.startPage(page,rows);
        List<TbContent> list = contentMapper.selectByExample(example);

        PageInfo<TbContent> pageInfo = new PageInfo<TbContent>(list);
        EasyUiDatagrid datagrid = new EasyUiDatagrid();
        datagrid.setTotal(pageInfo.getTotal());
        datagrid.setRows(pageInfo.getList());
        return datagrid;
    }

    @Override
    public List<TbContent> queryContentByCid(Long cid) {
        TbContentExample example = new TbContentExample();
        example.createCriteria().andCategoryIdEqualTo(cid);
        List<TbContent> list = contentMapper.selectByExampleWithBLOBs(example);
        return list;
    }

}
