package com.demo.cache.redisson;

import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

public class redissonDemo {
    public static void main(String[] args) throws InterruptedException {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://127.0.0.1:6379");

        final RedissonClient client = Redisson.create(config);
        RMap<String, String> rMap = client.getMap("map1");
        RLock lock = client.getLock("lock1");

        try {
            lock.lock();
            for (int i = 0; i< 15; i++){
                rMap.put("rkey:"+i, "111rvalue:" +i);
            }

            while (true){
                Thread.sleep(2000);
                System.out.println(rMap.get("rkey:10"));
            }
        }finally {
            lock.unlock();
        }
    }
}
