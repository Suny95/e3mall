package com.e3mall.cart.service;

import com.e3mall.common.pojo.E3Result;
import com.e3mall.pojo.TbItem;

import java.util.List;

/**
 * Created by Suny on 2017/6/23.
 */
public interface CartService {

    E3Result addCart(Long userId, Long itemId, Integer num);
    E3Result mergeCart(Long userId, List<TbItem> items);
    List<TbItem> queryCartList(Long userId);
    E3Result updateCartNum(Long userId, Long itemId,Integer num);
    E3Result deleteCartItem(Long userId, Long itemId);
    E3Result clearCartList(Long userId);
}
