package com.e3mall.activemqtest;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Test;

import javax.jms.*;
import java.io.IOException;

/**
 * Created by Suny on 2017/6/21.
 */
public class ActiveMqTest {

    @Test
    public void testQueue() throws JMSException {
        //创建连接工厂对象,需指定服务端ip与端口
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.56.204:61616");
        //使用工厂创建一个连接对象
        Connection connection = connectionFactory.createConnection();
        //开启连接
        connection.start();
        //使用连接对象创建一个session对象
        /**
         * 参数一: 是否开启事务,true,开启,第二个参数忽略
         * 参数二: 当第一个参数为false时才有效, 是消息的应答模式
         * 1. 自动应答
         * 2. 手动应答    一般都是自动应答
         */
        Session session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
        //使用session创建一个目标destination对象
        /**
         * 参数: 队列的名称
         */
        Queue queue = session.createQueue("testQueue");
        //使用session创建一个producer对象
        MessageProducer producer = session.createProducer(queue);
        //创建一个message对象
        TextMessage textMessage = session.createTextMessage("Hello ActiveMq");
        //发送消息
        producer.send(textMessage);
        //释放资源
        producer.close();
        session.close();
        connection.close();
    }

    @Test
    public void testActiveMqConsumer() throws JMSException, IOException {
        //创建连接工厂对象,指定服务ip与端口
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.56.204:61616");
        //使用工厂对象创建连接
        Connection connection = connectionFactory.createConnection();
        //开启连接
        connection.start();
        //使用连接创建session
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //使用session创建destination对象,指定要接收的队列名称
        Queue queue = session.createQueue("spring-queue");
        //使用session创建消费者
        MessageConsumer consumer = session.createConsumer(queue);
        //使用消费者监听消息队列
        consumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                try {
                    TextMessage textMessage = (TextMessage) message;
                    String text = textMessage.getText();
                    System.out.println(text);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        System.in.read();
        //释放资源
        consumer.close();
        session.close();
        connection.close();
    }

    @Test
    public void testTopicProducer() throws JMSException {
        //创建连接工厂,指定服务端IP和端口
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.56.204:61616");
        //通过工厂创建连接
        Connection connection = connectionFactory.createConnection();
        //开启连接
        connection.start();
        //通过连接创建session
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //通过session创建destination对象
        Topic topic = session.createTopic("testTopic");
        //通过session创建消费者
        MessageProducer producer = session.createProducer(topic);
        //创建消息对象
        TextMessage message = session.createTextMessage("topic test");
        //发送消息
        producer.send(message);
        //释放资源
        producer.close();
        session.close();
        connection.close();
    }

    @Test
    public void testTopicConsumer() throws JMSException, IOException {
        //创建连接工厂对象,指定服务ip与端口
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.56.204:61616");
        //使用工厂对象创建连接
        Connection connection = connectionFactory.createConnection();
        //开启连接
        connection.start();
        //使用连接创建session
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //使用session创建destination对象,指定要接收的队列名称
        Topic topic = session.createTopic("testTopic");
        //使用session创建消费者
        MessageConsumer consumer = session.createConsumer(topic);
        //使用消费者监听消息队列
        consumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                    TextMessage textMessage = (TextMessage) message;
                try {
                    String text = textMessage.getText();
                    System.out.println(text);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        System.in.read();
        //释放资源
        consumer.close();
        session.close();
        connection.close();
    }
}
