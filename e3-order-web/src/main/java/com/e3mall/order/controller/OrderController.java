package com.e3mall.order.controller;

import com.e3mall.cart.service.CartService;
import com.e3mall.common.pojo.E3Result;
import com.e3mall.order.pojo.OrderInfo;
import com.e3mall.order.service.OrderService;
import com.e3mall.pojo.TbItem;
import com.e3mall.pojo.TbUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 订单管理Controller
 * Created by Suny on 2017/6/24.
 */
@SuppressWarnings("ALL")
@Controller
public class OrderController {

    @Autowired
    private CartService cartService;
    @Autowired
    private OrderService orderService;

    @RequestMapping("/order/order-cart")
    public String showOrderList(HttpServletRequest request) {
        //获取用户信息
        TbUser user = (TbUser) request.getAttribute("loginUser");
        //根据用户id去收获地址
        //使用静态数据
        //取支付方式列表
        //静态数据
        //根据用户id查询购物车列表
        List<TbItem> cartList = cartService.queryCartList(user.getId());
        //将购物车列表传递到页面展示
        request.setAttribute("cartList",cartList);
        //返回页面
        return "order-cart";
    }

    @RequestMapping(value = "/order/create",method = RequestMethod.POST)
    public String createOrder(OrderInfo orderInfo, HttpServletRequest request) {
        //取用户信息,
        TbUser user = (TbUser) request.getAttribute("loginUser");
        //补全订单信息
        orderInfo.setUserId(user.getId());
        orderInfo.setBuyerNick(user.getUsername());
        //调用service创建订单
        E3Result result = orderService.createOrder(orderInfo);
        //创建订单完毕,清空购物车
        if (result.getStatus() == 200) {
            cartService.clearCartList(user.getId());
        }
        //取得订单号
        String orderId = (String) result.getData();
        //传递订单号到页面展示
        request.setAttribute("orderId",orderId);
        //传递总金额到页面展示
        request.setAttribute("payment",orderInfo.getPayment());
        //返回试图
        return "success";
    }
}
