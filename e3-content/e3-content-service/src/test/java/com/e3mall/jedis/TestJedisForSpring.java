package com.e3mall.jedis;

import com.e3mall.common.redis.JedisClient;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by Suny on 2017/6/20.
 */
public class TestJedisForSpring {

    @Test
    public void testJedisForSpring() {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-redis.xml");
        JedisClient jedis = ctx.getBean(JedisClient.class);
        jedis.set("testSpring1","testSpring1");
        String s = jedis.get("testSpring1");
        System.out.println(s);
    }
}
