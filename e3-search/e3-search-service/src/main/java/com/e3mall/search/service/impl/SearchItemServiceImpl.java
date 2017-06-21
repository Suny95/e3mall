package com.e3mall.search.service.impl;

import com.e3mall.common.pojo.E3Result;
import com.e3mall.common.pojo.SearchItem;
import com.e3mall.search.mapper.ItemMapper;
import com.e3mall.search.service.SearchItemService;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Suny on 2017/6/20.
 */
@Service
public class SearchItemServiceImpl implements SearchItemService {

    @Autowired
    private ItemMapper itemMapper;
    @Autowired
    private SolrServer solrServer;

    @Override
    public E3Result importAllItems() {
        try {
            //查询所有商品
            List<SearchItem> itemList = itemMapper.queryItemList();
            //将商品添加至索引库
            for (SearchItem item : itemList) {
                //创建文档对象
                SolrInputDocument document = new SolrInputDocument();
                //向文档中添加域
                document.addField("id",item.getId());
                document.addField("item_title",item.getTitle());
                document.addField("item_sell_point",item.getSellPoint());
                document.addField("item_price",item.getPrice());
                document.addField("item_image",item.getImage());
                document.addField("item_category_name",item.getCategoryName());
                //添加到索引库
                solrServer.add(document);
            }
            //提交
            solrServer.commit();
            return new E3Result(200,"插入成功",null);
        } catch (Exception e) {
            e.printStackTrace();
            return new E3Result(500,"插入失败",null);
        }
    }
}
