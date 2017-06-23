package com.e3mall.common.redis;

import java.util.List;

public interface JedisClient {

	String set(String key, String value);
	String get(String key);
	Long del(String key);
	Boolean exists(String key);
	Boolean hexists(String key, String field);
	Long expire(String key, int seconds);
	Long ttl(String key);
	Long incr(String key);
	Long hincrby(String key, String field,Long val);
	Long hset(String key, String field, String value);
	String hget(String key, String field);
	Long hdel(String key, String... field);
	List<String> hvals(String key);
}
