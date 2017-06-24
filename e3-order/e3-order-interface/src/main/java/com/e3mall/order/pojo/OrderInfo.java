package com.e3mall.order.pojo;

import com.e3mall.pojo.TbOrder;
import com.e3mall.pojo.TbOrderItem;
import com.e3mall.pojo.TbOrderShipping;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Suny on 2017/6/24.
 */
public class OrderInfo extends TbOrder implements Serializable {

    //商品明细
    private List<TbOrderItem> orderItems;
    //商品配送地址
    private TbOrderShipping orderShipping;

    public List<TbOrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<TbOrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public TbOrderShipping getOrderShipping() {
        return orderShipping;
    }

    public void setOrderShipping(TbOrderShipping orderShipping) {
        this.orderShipping = orderShipping;
    }
}
