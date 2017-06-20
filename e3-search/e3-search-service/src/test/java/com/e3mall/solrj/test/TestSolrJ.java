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
}
