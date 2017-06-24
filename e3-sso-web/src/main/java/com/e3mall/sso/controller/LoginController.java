package com.e3mall.sso.controller;

import com.e3mall.common.pojo.E3Result;
import com.e3mall.common.utils.CookieUtils;
import com.e3mall.sso.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录功能Controller
 * Created by Suny on 2017/6/23.
 */
@SuppressWarnings("ALL")
@Controller
public class LoginController {

    @Autowired
    private LoginService loginService;
    @Value("${TOKEN_KEY}")
    private String TOKEN_KEY;

    @RequestMapping("page/login")
    public String showLogin(String redirect, Model model) {
        //获取回调url传递到页面
        model.addAttribute("redirect",redirect);
        return "login";
    }

    @RequestMapping(value = "user/login", method = RequestMethod.POST)
    @ResponseBody
    public E3Result login(String username, String password,
                          HttpServletRequest request, HttpServletResponse respons) {
        E3Result result = loginService.userLogin(username, password);
        if (result.getStatus() == 200) {
            String token = (String) result.getData();
            CookieUtils.setCookie(request,respons,TOKEN_KEY,token);
        }
        return result;
    }
}
