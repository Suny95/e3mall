package com.e3mall.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Suny on 2017/6/17.
 */
@Controller
public class PageController {

    @RequestMapping("/")
    public String toIndex() {
        return "index";
    }

    @RequestMapping("/{page}")
    public String toIndex(@PathVariable String page) {
        return page;
    }


}



