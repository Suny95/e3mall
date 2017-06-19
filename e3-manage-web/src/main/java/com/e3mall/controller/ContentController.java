package com.e3mall.controller;

import com.e3mall.common.pojo.E3Result;
import com.e3mall.common.pojo.EasyUiDatagrid;
import com.e3mall.content.service.ContentService;
import com.e3mall.pojo.TbContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by Suny on 2017/6/19.
 */
@Controller
public class ContentController {

    @Autowired
    private ContentService contentService;

    @RequestMapping("/content/save")
    @ResponseBody
    public E3Result addContent(TbContent content) {
        E3Result result = contentService.addContent(content);
        return result;
    }

    @RequestMapping(value = "/content/query/list")
    @ResponseBody
    public EasyUiDatagrid queryContentById(Long categoryId, Integer page,Integer rows) {
        EasyUiDatagrid datagrid = contentService.queryContentById(categoryId, page, rows);
        return datagrid;
    }

}
