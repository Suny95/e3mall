package com.e3mall.testpublish;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by Suny on 2017/6/19.
 */
public class TestPublish {

    @Test
    public void publish () throws Exception {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-*.xml");

        System.out.println("-----------服务已启动-----------");
       System.in.read();
        System.out.println("-----------服务已关闭-----------");
    }
}
