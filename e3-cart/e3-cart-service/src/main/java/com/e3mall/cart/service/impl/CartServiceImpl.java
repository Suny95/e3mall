package com.e3mall.cart.service.impl;

import com.e3mall.cart.service.CartService;
import com.e3mall.common.pojo.E3Result;
import com.e3mall.common.redis.JedisClient;
import com.e3mall.common.utils.JsonUtils;
import com.e3mall.mapper.TbItemMapper;
import com.e3mall.pojo.TbItem;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Suny on 2017/6/23.
 */
@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private JedisClient jedisClient;
    @Autowired
    private TbItemMapper itemMapper;

    @Value("${REDIS_CART_PRE}")
    private String REDIS_CART_PRE;

    @Override
    public E3Result addCart(Long userId, Long itemId, Integer num) {
        //将购物车列表添加到redis中
        //判断商品是否存在
        Boolean hexists = jedisClient.hexists(REDIS_CART_PRE + ":" + userId, itemId + "");
        if (hexists) {
        //如果存在,数量相加
            String json = jedisClient.hget(REDIS_CART_PRE + ":" + userId, itemId + "");
            TbItem tbItem = JsonUtils.jsonToPojo(json, TbItem.class);
            tbItem.setNum(tbItem.getNum() + num);

            //将商品加入redis
            jedisClient.hset(REDIS_CART_PRE + ":" + userId, itemId + "",JsonUtils.objectToJson(tbItem));
        }
        //如果不存在,查询商品信息
        TbItem tbItem = itemMapper.selectByPrimaryKey(itemId);
        //设置购物车商品数量
        tbItem.setNum(num);
        //取一张图片
        String image = tbItem.getImage();
        if (StringUtils.isNotBlank(image)) {
            tbItem.setImage(image.split(",")[0]);
        }
        //将商品添加到redis中
        jedisClient.hset(REDIS_CART_PRE + ":" + userId, itemId + "",JsonUtils.objectToJson(tbItem));
        //返回成功
        return new E3Result(200,"OK",null);
    }

    @Override
    public E3Result mergeCart(Long userId, List<TbItem> items) {
        //遍历商品列表
        //将列表添加到redis
        //判断redis中是否有相同的商品
        //如果有,数量相加
        //如果没有,直接添加
        //与添加购物车功能类似,可以直接调用
        for (TbItem item : items) {
            addCart(userId,item.getId(),item.getNum());
        }
        return new E3Result(200,"OK",null);
    }

    @Override
    public List<TbItem> queryCartList(Long userId) {
        //根据key查询购物车所有商品
        List<String> hvals = jedisClient.hvals(REDIS_CART_PRE + ":" + userId);
        //准备新集合
        List<TbItem> itemList = new ArrayList<>();
        //遍历所有json
        for (String hval : hvals) {
            //转为POJO对象,添加到准备好的集合中
            TbItem item = JsonUtils.jsonToPojo(hval,TbItem.class);
            itemList.add(item);
        }
        //返回集合
        return itemList;
    }

    @Override
    public E3Result updateCartNum(Long userId, Long itemId, Integer num) {
        //更新redis中商品的数量
        //根据用户id和商品id查出商品
        String json = jedisClient.hget(REDIS_CART_PRE + ":" + userId, itemId + "");
        TbItem item = JsonUtils.jsonToPojo(json, TbItem.class);
        //更新商品
        item.setNum(num);
        //写入商品到reidis
        jedisClient.hset(REDIS_CART_PRE + ":" + userId, itemId + "", JsonUtils.objectToJson(item));
        return new E3Result(200,"OK",null);
    }

    @Override
    public E3Result deleteCartItem(Long userId, Long itemId) {
        //删除redis中对应的商品信息
        jedisClient.hdel(REDIS_CART_PRE + ":" + userId, itemId + "");
        return new E3Result(200,"OK",null);
    }

    @Override
    public E3Result clearCartList(Long userId) {
        //删除用户商品表数据
        jedisClient.del(REDIS_CART_PRE + ":" + userId);
        return new E3Result(200,"OK",null);
    }
}
