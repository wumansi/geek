package com.demo.cache.Service;

import com.demo.cache.Mapper.UserMapper;
import com.demo.cache.concurrent.RedisCount;
import com.demo.cache.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 分布式计数器.
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;

    //开启spring cache
    @Cacheable(key = "#id", value = "userCache")
    public User find(int id) {
        System.out.println(" ==> find " + id);
        return userMapper.find(id);
    }

    @Cacheable
    @Override
    public List<User> list() {
        return userMapper.list();
    }

    @Autowired
    private RedisCount redisCount;
    public int requestCount(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String key = sdf.format(new Date().getTime());
        System.out.println("计数：" + key);
        boolean isexist = redisCount.existKey(key);
        int result = redisCount.incrementInt(key, 1);
        if (!isexist){
            redisCount.setKeyExpire(key, 24);
        }
        return result;
    }

    public int reduceInventory(int increment) throws Exception {
        String goodId = "110210";
        System.out.println("计数：" + goodId);
        boolean isexist = redisCount.existKey(goodId);
        int curVal = redisCount.getByKey(goodId);
        if (!isexist || curVal < increment){
            throw new Exception("当前库存不足！");
        }
        int result = redisCount.decrementInt(goodId, increment);
        return result;
    }
}
