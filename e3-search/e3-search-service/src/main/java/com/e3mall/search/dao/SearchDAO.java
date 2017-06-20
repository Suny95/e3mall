package com.e3mall.search.dao;

import com.e3mall.common.pojo.SearchItem;
import com.e3mall.common.pojo.SearchResult;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Suny on 2017/6/20.
 */
@Repository
public class SearchDAO {

    @Autowired
    private SolrServer solrServer;

    /**
     * 搜索数据,关键字高亮显示
     * @param query
     * @return
     * @throws SolrServerException
     */
    public SearchResult search(SolrQuery query) throws SolrServerException {
        //根据查询条件查询数据
        QueryResponse response = solrServer.query(query);
        //获取非高亮字段
        SolrDocumentList results = response.getResults();
        //设置总记录数
        SearchResult result = new SearchResult();
        result.setRecordCount(results.getNumFound());
        //获取高亮字段
        Map<String, Map<String, List<String>>> map = response.getHighlighting();
        //定义集合存储查询结果
        List<SearchItem> itemList = new ArrayList<>();
        //遍历结果
        for (SolrDocument document : results) {
            SearchItem item = new SearchItem();
            //封装查询结果
            item.setId((String) document.get("id"));
            item.setCategoryName((String) document.get("item_category_name"));
            item.setImage((String) document.get("item_image"));
            item.setPrice((Long) document.get("item_price"));
            item.setSellPoint((String) document.get("item_sell_point"));
            //获取高亮字段
            List<String> list = map.get(document.get("id")).get("item_title");
            String title = null;
            if (list != null && list.size() > 0) {
                title = list.get(0);
            }else {
                title = (String) document.get("item_title");
            }
            //封装高亮字段
            item.setTitle(title);
            itemList.add(item);
        }
        //封装查询结果并返回
        result.setItemList(itemList);
        return result;
    }
}
