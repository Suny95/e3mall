package com.e3mall.sso.controller;

import com.e3mall.common.pojo.E3Result;
import com.e3mall.pojo.TbUser;
import com.e3mall.sso.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 注册功能Controller
 * Created by Suny on 2017/6/23.
 */
@Controller
public class RegisterController {

    @Autowired
    private RegisterService registerService;

    @RequestMapping("page/register")
    public String showRegister() {
        return "register";
    }

    @RequestMapping("/user/check/{param}/{type}")
    @ResponseBody
    public E3Result checkData(@PathVariable String param, @PathVariable Integer type) {
        E3Result result = registerService.checkData(param, type);
        return result;
    }

    @RequestMapping("user/register")
    @ResponseBody
    public E3Result register(TbUser user) {
        E3Result result = registerService.register(user);
        return result;
    }
}
