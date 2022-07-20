package com.demo.cache.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;

@Service
public class JedisAdapter implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(JedisAdapter.class);

    private Jedis jedis = null;

    private JedisPool jedisPool = null;

    /**
     * 初始化连接池.
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        jedisPool = new JedisPool("localhost", 6379);
    }

    /**
     * 获取Jedis连接.
     * @return
     */
    private Jedis getJedis(){
        try {
            jedis = jedisPool.getResource();
            return jedis;
        } catch (Exception e){
            logger.error("获取Jedis异常：" + e.getMessage());
            return null;
        } finally {
            if (jedis != null){
                try {
                    jedis.close();
                } catch (Exception e){
                    logger.error(e.getMessage());
                    return null;
                }
            }
        }
    }

    public long lpush(String key, String value){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            long result = jedis.lpush(key, value);
            return result;
        } catch (Exception e){
            logger.error("获取Jedis异常：" + e.getMessage());
            return 0;
        } finally {
            if (jedis != null){
                try {
                    jedis.close();
                } catch (Exception e){
                    logger.error(e.getMessage());
                }
            }
        }
    }

    public List<String> brpop(String key, int timeout){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.brpop(timeout, key);
        } catch (Exception e){
            logger.error("获取Jedis异常：" + e.getMessage());
            return null;
        } finally {
            if (jedis != null){
                try {
                    jedis.close();
                } catch (Exception e){
                    logger.error(e.getMessage());
                }
            }
        }
    }
}
