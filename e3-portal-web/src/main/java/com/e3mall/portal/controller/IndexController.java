package com.e3mall.portal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Suny on 2017/6/18.
 */
@Controller
public class IndexController {

    @RequestMapping("index")
    public String showIndex() {
        return "index";
    }
}
