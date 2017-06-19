package com.e3mall.portal.controller;

import com.e3mall.content.service.ContentService;
import com.e3mall.pojo.TbContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * Created by Suny on 2017/6/18.
 */
@Controller
public class IndexController {

    @Autowired
    private ContentService contentService;

    @Value("${CONTENT_IMAGE_ID}")
    private Long CONTENT_IMAGE_ID;

    @RequestMapping("/index")
    public String showIndex(Model model) {
        List<TbContent> ad1List = contentService.queryContentByCid(CONTENT_IMAGE_ID);
        model.addAttribute("ad1List",ad1List);
        return "index";
    }
}
