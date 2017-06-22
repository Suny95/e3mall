package com.e3mall.activemqtest;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

/**
 * Created by Suny on 2017/6/22.
 */
public class ActiveMqSpring {

    @Test
    public void testProducer() {
        //创建spring容器
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring/applicationContext-activemq.xml");
        //获取jms模板对象
        JmsTemplate jmsTemplate = context.getBean(JmsTemplate.class);
        //获取destination对象
        Destination queueDestination = (Destination) context.getBean("queueDestination");
        //发送消息消息到目标对象
        jmsTemplate.send(queueDestination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                return session.createTextMessage("test queue spring");
            }
        });
    }
}
