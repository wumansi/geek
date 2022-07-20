package com.demo.cache.sentinel;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;

import java.util.HashSet;
import java.util.Set;

public final class SentinelJedis {
    private static Integer MAX_TOTAL = 16;

    private static Integer MAX_IDLE = 12;

    private static Integer MAX_WAIT_MILLIS = 10000;
    //客户端超时时间配置
    private static Integer TIMEOUT = 10000;
    //在borrow(用)一个jedis实例时，是否提前进行validate(验证)操作；
    //如果为true，则得到的jedis实例均是可用的
    private static Boolean TEST_ON_BORROW = true;
    //在空闲时检查有效性, 默认false
    private static Boolean TEST_WHILE_IDLE = true;
    //是否进行有效性检查
    private static Boolean TEST_ON_RETURN = true;

    private static JedisSentinelPool POOL = createJedisPool();

    private static JedisSentinelPool createJedisPool() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(MAX_TOTAL);
        config.setMaxIdle(MAX_IDLE);
        config.setMaxWaitMillis(MAX_WAIT_MILLIS );
        config.setTestOnBorrow(TEST_ON_BORROW);
        config.setTestWhileIdle(TEST_WHILE_IDLE);
        config.setTestOnReturn(TEST_ON_RETURN);
        String masterName = "mymaster";
        Set<String> setinels = new HashSet<>();
        setinels.add(new HostAndPort("127.0.0.1",26379).toString());
        setinels.add(new HostAndPort("127.0.0.1",26380).toString());
        JedisSentinelPool pool = new JedisSentinelPool(masterName, setinels, config, TIMEOUT, null);
        return pool;
    }

    public static Jedis getJedis(){
        return POOL.getResource();
    }

    public static void close() {
        POOL.close();
    }
}
