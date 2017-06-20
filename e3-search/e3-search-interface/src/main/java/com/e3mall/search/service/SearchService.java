package com.e3mall.search.service;

import com.e3mall.common.pojo.SearchResult;

/**
 * Created by Suny on 2017/6/20.
 */
public interface SearchService {
    public SearchResult search(String keyword, Integer page, Integer pageSize) throws Exception;
}
