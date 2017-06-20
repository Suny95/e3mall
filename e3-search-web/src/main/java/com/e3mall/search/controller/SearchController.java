package com.e3mall.search.controller;

import com.e3mall.common.pojo.SearchResult;
import com.e3mall.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by Suny on 2017/6/20.
 */
@Controller
public class SearchController {
    @Autowired
    private SearchService searchService;

    @Value("${SEARCH_RESULT_ROWS}")
    public Integer SEARCH_RESULT_ROWS;

    @RequestMapping("/search")
    public String search(String keyword, @RequestParam(defaultValue = "1") Integer page, Model model) {
        try {
            keyword = new String(keyword.getBytes("ISO-8859-1"),"UTF-8");
                //查询出结果,转发到页面
               SearchResult search = searchService.search(keyword, page, SEARCH_RESULT_ROWS);
               model.addAttribute("query",keyword);
               model.addAttribute("totalPages",search.getTotalPages());
               model.addAttribute("page",page);
               model.addAttribute("recourdCount",search.getRecordCount());
               model.addAttribute("itemList",search.getItemList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "search";
    }
}
