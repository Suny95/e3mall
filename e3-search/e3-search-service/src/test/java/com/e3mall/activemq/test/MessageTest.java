package com.e3mall.activemq.test;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 * Created by Suny on 2017/6/22.
 */
public class MessageTest {

    @Test
    public void testConsumer() throws IOException {
        //启动spring容器,消息监听器就会启动
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring/applicationContext-activemq.xml");

        //暂停,等待接收消息
        System.in.read();
    }
}
