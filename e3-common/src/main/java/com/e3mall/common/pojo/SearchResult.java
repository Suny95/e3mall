package com.e3mall.common.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Suny on 2017/6/20.
 */
public class SearchResult implements Serializable {
    //总记录数
    private Long recordCount;
    private Long totalPages;
    private List<SearchItem> itemList;

    public Long getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(Long recordCount) {
        this.recordCount = recordCount;
    }

    public Long getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Long totalPages) {
        this.totalPages = totalPages;
    }

    public List<SearchItem> getItemList() {
        return itemList;
    }

    public void setItemList(List<SearchItem> itemList) {
        this.itemList = itemList;
    }
}
