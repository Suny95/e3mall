package com.e3mall.order.interceptor;

import com.e3mall.cart.service.CartService;
import com.e3mall.common.pojo.E3Result;
import com.e3mall.common.utils.CookieUtils;
import com.e3mall.common.utils.JsonUtils;
import com.e3mall.pojo.TbItem;
import com.e3mall.pojo.TbUser;
import com.e3mall.sso.service.TokenService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录拦截器
 * Created by Suny on 2017/6/24.
 */
public class LoginInterceptor implements HandlerInterceptor {

    @Value("${SSO_URL}")
    private String SSO_URL;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private CartService cartService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //从cookie中取token
        String token = CookieUtils.getCookieValue(request, "token");
        //判断token是否存在
        if (StringUtils.isBlank(token)) {
            //如果不存在.未登录,跳转到登录页面,登录成功后,在跳转回当前请求的url
            response.sendRedirect(SSO_URL + "?redirect=" + request.getRequestURL());
            return false;
        }
        //如果存在,调用sso系统服务,根据token取用户信息
        E3Result result = tokenService.queryUserByToken(token);
        if (result.getStatus() != 200) {
            //如果取不到,登录过期,跳转到登录页面
            response.sendRedirect(SSO_URL + "?redirect=" + request.getRequestURL());
            return false;
        }
        //如果取到,将用户信息传递到handler
        TbUser user = (TbUser) result.getData();
        request.setAttribute("loginUser",user);
        //判断cookie中是否有购物车信息,如果有,合并到服务端
        String cartJson = CookieUtils.getCookieValue(request, "cart", true);
        if (StringUtils.isNotBlank(cartJson)) {
            cartService.mergeCart(user.getId(), JsonUtils.jsonToList(cartJson, TbItem.class));
        }
        //放行
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView mv) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
