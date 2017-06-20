package com.e3mall.search.mapper;

import com.e3mall.common.pojo.SearchItem;

import java.util.List;

/**
 * Created by Suny on 2017/6/20.
 */
public interface ItemMapper {

    List<SearchItem> queryItemList();
}
