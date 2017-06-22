package com.e3mall.service.impl;

import com.e3mall.common.pojo.E3Result;
import com.e3mall.common.pojo.EasyUiDatagrid;
import com.e3mall.common.redis.JedisClient;
import com.e3mall.common.utils.IDUtils;
import com.e3mall.common.utils.JsonUtils;
import com.e3mall.mapper.TbItemDescMapper;
import com.e3mall.mapper.TbItemMapper;
import com.e3mall.pojo.TbItem;
import com.e3mall.pojo.TbItemDesc;
import com.e3mall.pojo.TbItemDescExample;
import com.e3mall.pojo.TbItemExample;
import com.e3mall.service.ItemService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.jms.*;
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
    @Autowired
    private JmsTemplate jmsTemplate;
    //resource会根据属性名找ID相同的bean,找不到才会按照类型查找
    @Resource
    private Destination topicDestination;

    @Autowired
    private JedisClient jedisClient;

    /**
     * 商品key
     */
    @Value("${REDIS_ITEM_INFO}")
    private String REDIS_ITEM_INFO;
    /**
     商品key生存时间,默认一小时
     */
    @Value("${REDIS_ITEM_CACHE_EXPIRE}")
    private Integer REDIS_ITEM_CACHE_EXPIRE;

    /**
     * 根据ID查询商品
     * @param id
     * @return
     */
    @Override
    public TbItem queryTbItemById(Long id) {
        try {
            //查询缓存
            String json = jedisClient.get(REDIS_ITEM_INFO + ":" + id + ":BASE");
            if (StringUtils.isNotBlank(json)) {
                return JsonUtils.jsonToPojo(json, TbItem.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //缓存中没有,再去数据库查
        //根据主键查询商品
        TbItem tbItem = itemMapper.selectByPrimaryKey(id);

        try {
            //商品查询完成之后设置缓存
            jedisClient.set(REDIS_ITEM_INFO + ":" + id + ":BASE",JsonUtils.objectToJson(tbItem));
            //设置key的过期时间
            jedisClient.expire(REDIS_ITEM_INFO + ":" + id + ":BASE",REDIS_ITEM_CACHE_EXPIRE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tbItem;
    }

    @Override
    public TbItemDesc queryItemDescById(Long itemId) {
        try {
            //查询缓存
            String json = jedisClient.get(REDIS_ITEM_INFO + ":" + itemId + ":DESC");
            if (StringUtils.isNotBlank(json)) {
                return JsonUtils.jsonToPojo(json,TbItemDesc.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //根据商品id查询商品描述信息
        TbItemDesc itemDesc = itemDescMapper.selectByPrimaryKey(itemId);

        try {
            //设置缓存
            jedisClient.set(REDIS_ITEM_INFO + ":" + itemId + ":DESC",JsonUtils.objectToJson(itemDesc));
            //设置key的生存时间
            jedisClient.expire(REDIS_ITEM_INFO + ":" + itemId + ":DESC",REDIS_ITEM_CACHE_EXPIRE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return itemDesc;
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
        final long itemId = IDUtils.genItemId();
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

        //商品信息添加完毕,向其他系统发送消息
        jmsTemplate.send(topicDestination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                //将商品id发送给其他系统
                TextMessage message = session.createTextMessage(itemId + "");
                return message;
            }
        });


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

        try {
            //修改商品数据需要删除redis中的key
            jedisClient.del(REDIS_ITEM_INFO + ":" + tbItem.getId() + ":BASE");
            jedisClient.del(REDIS_ITEM_INFO + ":" + tbItem.getId() + ":DESC");
        } catch (Exception e) {
            e.printStackTrace();
        }

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

        try {
            //删除商品数据时删除redis中的key
            for (Long id : ids) {
                jedisClient.del(REDIS_ITEM_INFO + ":" + id + ":BASE");
                jedisClient.del(REDIS_ITEM_INFO + ":" + id + ":DESC");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

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
