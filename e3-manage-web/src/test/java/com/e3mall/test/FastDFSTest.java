package com.e3mall.test;

import com.e3mall.common.utils.FastDFSClient;
import org.csource.fastdfs.*;
import org.junit.Test;

/**
 * Created by Suny on 2017/6/18.
 */
public class FastDFSTest {

    @Test
    public void fastDFSTest() throws Exception {
        //创建一个配置文件,内容是tracker服务器的地址
        //使用全局对象加载配置文件
        ClientGlobal.init("D:\\workspaces\\IDEA\\e3parent\\e3-manage-web\\src\\main\\resources\\conf\\client.properties");
        //创建一个TrackerClient对象
        TrackerClient trackerClient = new TrackerClient();
        //通过TrackerClient获得一个TrackerServer对象
        TrackerServer trackerServer = trackerClient.getConnection();
        //创建一个StorageServer的引用,可以是null
        StorageServer storageServer = null;
        //创建一个StorageClient,参数需要TrackerServer和StorageServer
        StorageClient storageClient = new StorageClient(trackerServer, storageServer);
        //使用StorageClient上传文件
        String[] file = storageClient.upload_file("C:\\Users\\Public\\Pictures\\Sample Pictures\\code.jpg", "jpg", null);
        for (String s : file) {
            System.out.println(s);
        }
    }

    @Test
    public void fastClientUtilsTest() throws Exception {
        FastDFSClient fastDFSClient = new FastDFSClient("D:\\workspaces\\IDEA\\e3parent\\e3-manage-web\\src\\main\\resources\\conf\\client.properties");
        String s = fastDFSClient.uploadFile("C:\\Users\\Public\\Pictures\\Sample Pictures\\Jellyfish.jpg");
        System.out.println(s);
    }
}
