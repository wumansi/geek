package com.demo.cache;

import com.alibaba.fastjson.JSON;
import com.demo.cache.sentinel.SentinelJedis;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootApplication(scanBasePackages = {"com.demo.cache"})
@MapperScan("com.demo.cache.Mapper")
@EnableCaching
public class CacheApplication {

	public static void main(String[] args) {
		// 最简单的demo
//		Jedis jedis = new Jedis("localhost",6379);
//		System.out.println(jedis.info());
//		jedis.set("uptime", new Long(System.currentTimeMillis()).toString());
//		System.out.println(jedis.get("uptime"));

//		Jedis sjedis = SentinelJedis.getJedis();
//		System.out.println(sjedis.info());
//		sjedis.set("uptime2", new Long(System.currentTimeMillis()).toString());
//		System.out.println(sjedis.get("uptime2"));
//		SentinelJedis.close();

//		Map<String, String> map = new HashMap<>();
//		for (int i = 0; i< 20; i++){
//			map.put("name" + i,"sisi" + i);
//		}

		SpringApplication.run(CacheApplication.class, args);
	}

}
