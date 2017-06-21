package com.e3mall.solrj.test;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by Suny on 2017/6/21.
 */
public class TestSolrCloud {

    @Test
    public void testSolrCloudAdd() throws IOException, SolrServerException {
        //创建solrCloud对象,指定集群zookeeper地址
        CloudSolrServer solrServer = new CloudSolrServer("192.168.56.204:2181,192.168.56.204:2182,192.168.56.204:2183");
        //设置defaultCollection属性
        solrServer.setDefaultCollection("collection2");
        //创建一个文档对象
        SolrInputDocument document = new SolrInputDocument();
        //向文档中添加域
        document.addField("id","solrCloud1");
        document.addField("item_title","标题1");
        //将文件写入索引库
        solrServer.add(document);
        //提交
        solrServer.commit();
    }

    @Test
    public void testSolrCloudQuery() throws SolrServerException {
        CloudSolrServer solrServer = new CloudSolrServer("192.168.56.204:2181,192.168.56.204:2182,192.168.56.204:2183");
        solrServer.setDefaultCollection("collection2");
        SolrQuery query = new SolrQuery("*:*");
        QueryResponse response = solrServer.query(query);
        SolrDocumentList results = response.getResults();
        System.out.println("总记录数: " + results.getNumFound());

        for (SolrDocument result : results) {
            System.out.println("id: " + result.get("id"));
            System.out.println("title: " + result.get("item_title"));
        }
    }
}
