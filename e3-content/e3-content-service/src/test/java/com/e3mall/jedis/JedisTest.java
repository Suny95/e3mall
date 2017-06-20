package com.e3mall.jedis;

import org.junit.Test;
import redis.clients.jedis.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Suny on 2017/6/20.
 */
public class JedisTest {

    @Test
    public void testJedis() {
        //创建Jedis对象
        Jedis jedis = new Jedis("192.168.56.204", 6379);
        //使用jedis操作redis
        jedis.set("test1","aaaaaaaaaa");
        String s = jedis.get("test1");
        System.out.println(s);
        //关闭链接
        jedis.close();
    }

    @Test
    public void testJedisPool() {
        //创建jedis连接池
        JedisPool pool = new JedisPool("192.168.56.204", 6379);
        //获取jedis
        Jedis jedis = pool.getResource();
        //使用jedis操作redis
        String s = jedis.get("test1");
        System.out.println(s);
        //关闭jedis
        jedis.close();
        //关闭连接池
        pool.close();
    }

    @Test
    public void testJedisCluster() {
        //创建jedis集群对象,需要一个Set<HostAndPort>参数,Redis节点列表
        Set<HostAndPort> nodes = new HashSet<>();
        nodes.add(new HostAndPort("192.168.56.204",7001));
        nodes.add(new HostAndPort("192.168.56.204",7002));
        nodes.add(new HostAndPort("192.168.56.204",7003));
        nodes.add(new HostAndPort("192.168.56.204",7004));
        nodes.add(new HostAndPort("192.168.56.204",7005));
        nodes.add(new HostAndPort("192.168.56.204",7006));
        JedisCluster jedisCluster = new JedisCluster(nodes);
        //直接使用cluster对象操作redis,在系统中单例存在
        String s = jedisCluster.set("cluster1", "test first JedisCluster");
        //返回操作结果
        System.out.println(s);
        String s1 = jedisCluster.get("cluster1");
        System.out.println(s1);
        //系统关闭前,关闭cluster
        jedisCluster.close();
    }

    @Test
    public void testShardedJedisPool() {
        //集群连接池配置对象
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        //设置最大连接数
        poolConfig.setMaxTotal(50);
        //设置最高存活数
        poolConfig.setMaxIdle(10);

        //定义集群信息
        List<JedisShardInfo> shardInfo = new ArrayList<>();
        shardInfo.add(new JedisShardInfo("192.168.56.204",7001));
        shardInfo.add(new JedisShardInfo("192.168.56.204",7002));
        shardInfo.add(new JedisShardInfo("192.168.56.204",7003));
        shardInfo.add(new JedisShardInfo("192.168.56.204",7004));
        shardInfo.add(new JedisShardInfo("192.168.56.204",7005));
        shardInfo.add(new JedisShardInfo("192.168.56.204",7006));

        //定义集群连接池对象, 需要一个连接池配置对象和一个集群信息
        ShardedJedisPool shardedJedisPool = new ShardedJedisPool(poolConfig, shardInfo);

        //从连接池获取jedis分片对象
        ShardedJedis jedis = shardedJedisPool.getResource();
        String s = jedis.get("cluster1");
        System.out.println(s);

        //关闭链接,会检测链接是否有效,有效放回连接池,无效则重置状态
        jedis.close();

        //关闭连接池
        shardedJedisPool.close();
    }
}
