package com.e3mall.order.service;

import com.e3mall.common.pojo.E3Result;
import com.e3mall.order.pojo.OrderInfo;

/**
 * Created by Suny on 2017/6/24.
 */
public interface OrderService {

    E3Result createOrder(OrderInfo orderInfo);
}
