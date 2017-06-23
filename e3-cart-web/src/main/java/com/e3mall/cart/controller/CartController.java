package com.e3mall.cart.controller;

import com.e3mall.cart.service.CartService;
import com.e3mall.common.pojo.E3Result;
import com.e3mall.common.utils.CookieUtils;
import com.e3mall.common.utils.JsonUtils;
import com.e3mall.pojo.TbItem;
import com.e3mall.pojo.TbUser;
import com.e3mall.service.ItemService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 购物车列表Controller
 * Created by Suny on 2017/6/23.
 */
@Controller
@SuppressWarnings("all")
public class CartController {

    @Autowired
    private ItemService itemService;
    @Autowired
    private CartService cartService;

    /**
     * 默认时间为: 5 天
     */
    @Value("${COOKIE_CART_EXPIRE}")
    private Integer COOKIE_CART_EXPIRE;

    @RequestMapping("/cart/add/{itemId}")
    public String addCart(@PathVariable Long itemId, @RequestParam(value = "num", defaultValue = "1") Integer num,
                                        HttpServletRequest request, HttpServletResponse response) {
        //判断是否登录
        TbUser user = (TbUser) request.getAttribute("loginUser");
        if (user != null) {
            //如果登录,将数据加入数据库
            cartService.addCart(user.getId(),itemId,num);
            //返回逻辑视图
            return "cartSuccess";
        }

        //如果未登录,将数据加入cookie
        //用于判断购物车是否有相同到商品
        Boolean flag = false;
        //从cookie中查询购物车列表
        List<TbItem> cartList = queryCartListByCookie(request);
        //判断商品在商品列表中是否存在
        for (TbItem tbItem : cartList) {
            if (tbItem.getId() == itemId.longValue()) {
                //找到商品,数量相加
                tbItem.setNum(tbItem.getNum() + num);
                flag = true;
                //跳出循环
                break;
            }
        }
        //如果不存在,查询数据库
        if (!flag) {
            //对商品对象重新赋值
            TbItem item = itemService.queryTbItemById(itemId);
            //设置商品数量
            item.setNum(num);
            //取一张图片
            String image = item.getImage();
            if (StringUtils.isNotBlank(image)) {
                item.setImage(image.split(",")[0]);
            }
            //将商品添加到购物车列表
            cartList.add(item);
        }
        //将对象写入cookie,设置cookie有效时间,并且编码
        CookieUtils.setCookie(request,response,"cart",JsonUtils.objectToJson(cartList),
                COOKIE_CART_EXPIRE,true);
        //返回
        return "cartSuccess";
    }

    /**
     * 查询cookie
     * @param request
     * @return
     */
    public List<TbItem> queryCartListByCookie(HttpServletRequest request) {
        //获取cookie
        String json = CookieUtils.getCookieValue(request, "cart", true);
        //判断是否为null
        if (StringUtils.isBlank(json)) {
            return new ArrayList<>();
        }

        //如果不为null,将商品返回
        List<TbItem> list = JsonUtils.jsonToList(json, TbItem.class);
        return list;
    }

    /**
     * 更新cookie中购物车商品的数量
     * @param itemId
     * @param num
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("cart/update/num/{itemId}/{num}")
    @ResponseBody
    public E3Result updateCartNum(@PathVariable Long itemId, @PathVariable Integer num, HttpServletRequest request, HttpServletResponse response) {
        //判断是否登录
        //如果登录,到redis中更新商品信息
        TbUser user = (TbUser) request.getAttribute("loginUser");
        if (user != null) {
            E3Result result = cartService.updateCartNum(user.getId(), itemId, num);
            return result;
        }

        //未登录更新cookie
        //查询购物车列表
        List<TbItem> cartList = queryCartListByCookie(request);
        //遍历购物车更改数量
        for (TbItem tbItem : cartList) {
            tbItem.setNum(num);
        }
        //写入cookie
        CookieUtils.setCookie(request,response,"cart",JsonUtils.objectToJson(cartList),COOKIE_CART_EXPIRE,true);
        //返回结果
        return new E3Result(200,"OK",null);
    }

    @RequestMapping("/cart/delete/{itemId}")
    public String delCartItem(@PathVariable Long itemId, HttpServletResponse response, HttpServletRequest request) {
        //判断是否登录
        //如果登录,到redis中删除商品信息
        TbUser user = (TbUser) request.getAttribute("loginUser");
        if (user != null) {
            //调用服务 删除redis中的商品
            cartService.deleteCartItem(user.getId(), itemId);
            return "redirect:/cart/cart.html";
        }

        //从cookie中查询购物车列表
        List<TbItem> cartList = queryCartListByCookie(request);
        //遍历列表,找到要删除的删除,删除它
        for (TbItem tbItem : cartList) {
            if (tbItem.getId().longValue() == itemId) {
                cartList.remove(tbItem);
                break;
            }
        }
        //再将购物车列表写入cookie
        CookieUtils.setCookie(request,response,"cart",JsonUtils.objectToJson(cartList),COOKIE_CART_EXPIRE,true);
        //重定向逻辑视图到购物车页面
        return "redirect:/cart/cart.html";
    }

    @RequestMapping("cart/cart")
    public String showCartList(HttpServletRequest request, HttpServletResponse response) {
        //从cookie中获取购物车列表
        List<TbItem> cartList = queryCartListByCookie(request);

        //查询用户是否登录,如果登录,从redis中获取购物车列表
        TbUser user = (TbUser) request.getAttribute("loginUser");
        if (user != null) {
            //将cookie中的商品与服务端的商品进行合并
            cartService.mergeCart(user.getId(),cartList);
            //将cookie中的购物车删除
            CookieUtils.deleteCookie(request,response,"cart");

            //获取服务端购物车列表
            cartList = cartService.queryCartList(user.getId());
        }

        //如果未登录,从cookie中获取列表
        //将购物车列表传递到页面
        request.setAttribute("cartList",cartList);
        //返回逻辑视图
        return "cart";
    }
}
