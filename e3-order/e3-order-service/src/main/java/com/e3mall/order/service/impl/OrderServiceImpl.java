package com.e3mall.order.service.impl;

import com.e3mall.common.pojo.E3Result;
import com.e3mall.common.redis.JedisClient;
import com.e3mall.mapper.TbOrderItemMapper;
import com.e3mall.mapper.TbOrderMapper;
import com.e3mall.mapper.TbOrderShippingMapper;
import com.e3mall.order.pojo.OrderInfo;
import com.e3mall.order.service.OrderService;
import com.e3mall.pojo.TbOrderItem;
import com.e3mall.pojo.TbOrderShipping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by Suny on 2017/6/24.
 */
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private JedisClient jedisClient;
    @Autowired
    private TbOrderItemMapper orderItemMapper;
    @Autowired
    private TbOrderMapper orderMapper;
    @Autowired
    private TbOrderShippingMapper orderShippingMapper;
    @Value("${REDIS_ORDER_INIT_ID}")
    private String REDIS_ORDER_INIT_ID;
    @Value("${REDIS_ORDER_KEY}")
    private String REDIS_ORDER_KEY;
    @Value("${REDIS_DETAIL_GENERIC_ID}")
    private String REDIS_DETAIL_GENERIC_ID;

    @Override
    public E3Result createOrder(OrderInfo orderInfo) {
        //生成订单号,由reidis自增长生成
        //第一次生成id需要给其设置一个初始值
        if (!jedisClient.exists(REDIS_ORDER_KEY)) {
            jedisClient.set(REDIS_ORDER_KEY,REDIS_ORDER_INIT_ID);
        }
        //生成订单id
        String orderId = jedisClient.incr(REDIS_ORDER_KEY).toString();
        //补全orderInfo属性
        orderInfo.setOrderId(orderId);
        orderInfo.setCreateTime(new Date());
        orderInfo.setUpdateTime(orderInfo.getCreateTime());
        //1. 未付款 2. 已付款 3. 未发货 4. 已发货 5. 交易完成 6. 交易关闭
        orderInfo.setStatus(1);
        //插入订单表
        orderMapper.insertSelective(orderInfo);
        //向订单明细表插入数据
        List<TbOrderItem> orderItems = orderInfo.getOrderItems();
        for (TbOrderItem orderItem : orderItems) {
            //生成明细表id
            String orderDetailId = jedisClient.incr(REDIS_DETAIL_GENERIC_ID).toString();
            //补全pojo属性
            orderItem.setId(orderDetailId);
            orderItem.setOrderId(orderId);
            //插入数据
            orderItemMapper.insertSelective(orderItem);
        }
        //向订单物流表插入数据
        TbOrderShipping orderShipping = orderInfo.getOrderShipping();
        //补全属性
        orderShipping.setOrderId(orderId);
        orderShipping.setCreated(new Date());
        orderShipping.setUpdated(orderShipping.getCreated());
        //插入数据
        orderShippingMapper.insertSelective(orderShipping);
        //返回E3Result,包含订单id
        return new E3Result(200,"OK",orderId);
    }
}
