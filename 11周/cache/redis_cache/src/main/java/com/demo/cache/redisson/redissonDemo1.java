package com.demo.cache.redisson;


import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

//import redis.clients.jedis.params.SetParams;


public class redissonDemo1 {
    public static void main(String[] args){
        Config config = new Config();
        config.useSingleServer().setAddress("redis://127.0.0.1:6379");

        final RedissonClient client = Redisson.create(config);
        RLock lock = client.getLock("lock1");

        try {
            lock.lock();
            RMap<String, String> rMap = client.getMap("map1");

            for (int i =0; i< 15; i++){
                rMap.put("rkey:"+i, "rvalue:22222-"+i);
            }
            System.out.println(rMap.get("rkey:10"));
        } finally {
            lock.unlock();
        }
    }
}
