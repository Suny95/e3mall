package com.e3mall.cart.interceptor;

import com.e3mall.common.pojo.E3Result;
import com.e3mall.common.utils.CookieUtils;
import com.e3mall.pojo.TbUser;
import com.e3mall.sso.service.TokenService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录处理拦截器
 * Created by Suny on 2017/6/23.
 */
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private TokenService tokenService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //前置拦截器, 在handler执行之前拦截
        //从cookie中获取token
        String token = CookieUtils.getCookieValue(request, "token");
        //判断token是否有值
        //如果没有值,说明未登录,直接放行
        if (StringUtils.isBlank(token)) {
            return true;
        }

        //如果有,调用sso登录系统,根据token查询用户信息
        E3Result result = tokenService.queryUserByToken(token);
        //如果没取到用户信息,登录过期,放行
        if (result.getStatus() != 200) {
            return true;
        }
        //如果取到,已登录,放入request中,传递到handler,handler中判断request中是否有用户信息即可知道是否登录
        TbUser user = (TbUser) result.getData();
        request.setAttribute("loginUser",user);

        //放行
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView mv) throws Exception {
        //后置拦截器,在handler执行之后,返回ModelAndView之前
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //完成处理,返回ModelAndView之后,可以在这处理异常
    }
}
