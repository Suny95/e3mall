package com.e3mall.search.service.impl;

import com.e3mall.common.pojo.SearchResult;
import com.e3mall.search.dao.SearchDAO;
import com.e3mall.search.service.SearchService;
import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Suny on 2017/6/20.
 */
@Service
public class SearchServiceImpl implements SearchService {
    @Autowired
    private SearchDAO dao;

    @Override
    public SearchResult search(String keyword, Integer page, Integer pageSize) throws Exception {
        //创建查询对象
        SolrQuery query = new SolrQuery();

        //设置查询条件
        query.setQuery(keyword);
        query.set("df","item_title");
        query.setHighlight(true);
        query.addHighlightField("item_title");
        query.setHighlightSimplePre("<em>");
        query.setHighlightSimplePost("</em>");

        //设置分页信息
        query.setStart((page - 1) * pageSize);
        query.setRows(pageSize);

        //调用DAO查询数据
        SearchResult result = dao.search(query);

        //封装结果集
        //根据总记录数与每页条数计算总页数
        Long totalPage = (result.getRecordCount() + pageSize - 1) / pageSize;
        //重新封装结果
        result.setTotalPages(totalPage);

        //返回结果
        return result;
    }
}
