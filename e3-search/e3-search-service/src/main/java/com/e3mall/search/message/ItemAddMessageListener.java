package com.e3mall.search.message;

import com.e3mall.common.pojo.SearchItem;
import com.e3mall.search.mapper.ItemMapper;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * Created by Suny on 2017/6/22.
 */
public class ItemAddMessageListener implements MessageListener {

    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    private SolrServer solrServer;

    @Override
    public void onMessage(Message message) {
        try {
            //获取信息
            TextMessage textMessage = (TextMessage) message;
            String text = textMessage.getText();
            //将信息转为商品id
            Long itemId = Long.parseLong(text);

            //由于发送消息的过程是非常快的,可能后台系统事务还未提交,信息就发送了,所以这里可以先等待一会,等事务提交,数据库中有数据了再去查
            Thread.sleep(500);

            //查询商品信息
            SearchItem searchItem = itemMapper.queryItemById(itemId);
            //创建solr文档对象
            SolrInputDocument document = new SolrInputDocument();
            //向文档中添加域
            document.addField("id",searchItem.getId());
            document.addField("item_title",searchItem.getTitle());
            document.addField("item_sell_point",searchItem.getSellPoint());
            document.addField("item_price",searchItem.getPrice());
            document.addField("item_image",searchItem.getImage());
            document.addField("item_category_name",searchItem.getCategoryName());
            //将文档添加到索引库
            solrServer.add(document);
            //提交
            solrServer.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
