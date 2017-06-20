package com.e3mall.solrj.test;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by Suny on 2017/6/20.
 */
public class TestSolrJ {

    @Test
    public void testAddDocument() throws IOException, SolrServerException {
        //创建solor服务,连接solr
        HttpSolrServer server = new HttpSolrServer("http://192.168.56.204:8080/solr");
        //创建document文档
        SolrInputDocument document = new SolrInputDocument();
        //在文档中添加字段
        document.addField("id","id1");
        document.addField("item_title","Iphone");
        //将文档加入索引库
        server.add(document);
        //提交
        server.commit();
    }

    @Test
    public void testDel() throws IOException, SolrServerException {
        HttpSolrServer server = new HttpSolrServer("http://192.168.56.204:8080/solr");
        server.deleteById("id1");
        server.commit();
    }

    @Test
    public void testQuery() throws SolrServerException {
        HttpSolrServer server = new HttpSolrServer("http://192.168.56.204:8080/solr");
        SolrQuery query = new SolrQuery("id:id1");
        QueryResponse response = server.query(query);
        SolrDocumentList results = response.getResults();
        for (SolrDocument document : results) {
            System.out.println(document.getFieldValue("id"));
        }
    }

    @Test
    public void testQueryIndex() throws SolrServerException {
        //创建连接
        HttpSolrServer server = new HttpSolrServer("http://192.168.56.204:8080/solr");
        //创建查询对象
        SolrQuery query = new SolrQuery();
        //设置查询条件
        query.setQuery("*:*");
        //执行查询,返回一个Reponse对象
        QueryResponse response = server.query(query);
        //获取结果集合
        SolrDocumentList solrDocumentList = response.getResults();
        //遍历结果
        System.out.println("商品总数是: " + solrDocumentList.getNumFound());

        for(SolrDocument document : solrDocumentList) {
            System.out.println("id: " + document.get("id"));
            System.out.println("item_title: " + document.get("item_title"));
            System.out.println("item_sell_point: " + document.get("item_sell_point"));
            System.out.println("item_price: " + document.get("item_price"));
            System.out.println("item_image: " + document.get("item_image"));
            System.out.println("item_category_name: " + document.get("item_category_name"));
        }
    }

    @Test
    public void testHighLightingQuery() throws SolrServerException {
        //创建连接
        HttpSolrServer server = new HttpSolrServer("http://192.168.56.204:8080/solr");
        //创建查询对象
        SolrQuery query = new SolrQuery();
        //设置查询条件
        query.setQuery("手机");
        query.set("df","item_title");
        query.setHighlight(true);
        query.addHighlightField("item_title");
        query.setHighlightSimplePre("<em>");
        query.setHighlightSimplePost("</em>");
        //执行查询
        QueryResponse response = server.query(query);
        //获取高亮字段
        Map<String, Map<String, List<String>>> map = response.getHighlighting();
        //获取非高亮字段
        SolrDocumentList solrDocumentList = response.getResults();
        //遍历结果
        for (SolrDocument document : solrDocumentList) {
            System.out.println("id: " + document.get("id"));
            //获取高亮字段
            List<String> list = map.get(document.get("id")).get("item_title");
            String title = "";
            if (list != null && list.size() > 0) {
                title = list.get(0);
            }else {
                title = (String) document.get("item_title");
            }
            System.out.println("title: " + title);
            System.out.println("sellPoint: " + document.get("item_sell_point"));
            System.out.println("price: " + document.get("item_price"));
            System.out.println("image: " + document.get("item_image"));
            System.out.println("categoryName: " + document.get("item_category_name"));
        }

    }
}
