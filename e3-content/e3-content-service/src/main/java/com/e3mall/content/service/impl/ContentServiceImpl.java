package com.e3mall.content.service.impl;

import com.e3mall.common.pojo.E3Result;
import com.e3mall.common.pojo.EasyUiDatagrid;
import com.e3mall.common.redis.JedisClient;
import com.e3mall.common.utils.JsonUtils;
import com.e3mall.content.service.ContentService;
import com.e3mall.mapper.TbContentMapper;
import com.e3mall.pojo.TbContent;
import com.e3mall.pojo.TbContentExample;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    @Autowired
    private JedisClient jedisClient;
    @Value("${CONTENT_LIST}")
    private String CONTENT_LIST;

    @Override
    public E3Result addContent(TbContent content) {
        content.setCreated(new Date());
        content.setUpdated(content.getCreated());
        contentMapper.insertSelective(content);

        try {
            //在更新数据的时候,删除缓存,保证缓存同步
            jedisClient.hdel(CONTENT_LIST,content.getCategoryId().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

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
        //进来先查缓存,并且不能影响原有代码的执行
        try {
            String json = jedisClient.hget(CONTENT_LIST, cid + "");
            if (StringUtils.isNotBlank(json)) {
                List<TbContent> list = JsonUtils.jsonToList(json, TbContent.class);
                return list;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        TbContentExample example = new TbContentExample();
        example.createCriteria().andCategoryIdEqualTo(cid);
        List<TbContent> list = contentMapper.selectByExampleWithBLOBs(example);

        //查询完毕,将数据放入缓存
        try {
            String toJson = JsonUtils.objectToJson(list);
            jedisClient.hset(CONTENT_LIST, cid + "", toJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

}
