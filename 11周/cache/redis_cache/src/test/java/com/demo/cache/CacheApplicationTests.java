package com.demo.cache;

import com.demo.cache.concurrent.RedisMsgPubSubListener;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CacheApplicationTests {
	@Autowired
	CacheManager manager;

	@Autowired
	RedisTemplate redisTemplate;
	@Test
	public void test2() {
//		EhCacheCache userCache = (EhCacheCache) manager.getCache("userCache");
//		System.out.println("打印缓存===========");
		boolean isHasUptime = redisTemplate.hasKey("uptime");
		System.out.println("isHasUptime ：" + isHasUptime);
		System.out.println("user ：" + redisTemplate.hasKey("user"));
		System.out.println("isHasUptime ：" + isHasUptime);
		redisTemplate.delete("user");
		System.out.println("user ：" + redisTemplate.hasKey("user"));

		BoundValueOperations stringkey = redisTemplate.boundValueOps("stu");
		stringkey.set("stringValue");
		stringkey.set("stringValue", 1, TimeUnit.MINUTES);

		ValueOperations ops = redisTemplate.opsForValue();
		ops.set("stringKey", "stringValue");
		ops.set("stringValue", "stringValue", 1, TimeUnit.MINUTES);

		Boolean result = redisTemplate.delete("stringKey");
		System.out.println(result);
	}

	/**
	 * Redis分布式锁测试.
	 */
	@Test
	public void testRedisLock(){
		String key = "test";
		boolean islock = lock(key, 1, 50);
		if (!islock){
			System.out.println("获取锁失败！");
			return;
		}
		try {
			System.out.println("获取锁成功，开始执行...");
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}finally {
			unlock(key);
			System.out.println("执行完毕，成功释放锁！");
		}
	}

	/**
	 * 解锁.
	 * @param key 键
	 */
	private void unlock(String key) {
		redisTemplate.delete(key);
	}

	/**
	 * 加锁.
	 * @param key 键
	 * @param value 值
	 * @param timeout 超时时间
	 * @return 加锁结果
	 */
	public Boolean lock(String key, long value, long timeout){
		Boolean result = redisTemplate.opsForValue().setIfAbsent(key, value, timeout, TimeUnit.SECONDS);
		return result != null && result;
		// 2.1版本中才有setIfAbsent(K key, V value, long timeout, TimeUnit unit)方法
//		return (Boolean) redisTemplate.execute(new RedisCallback<Boolean>() {
//			@Override
//			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
//
//				JedisCommands commands = (JedisCommands)connection.getNativeConnection();
//				String result = commands.set(key, value, "NX", "PX", unit.toMillis(timeout));
//
//				return "OK".equals(result);
//			}
//		});
	}

	@Test
	public void subjava(){
		Jedis jedis = null;
		try {
			jedis = new Jedis("127.0.0.1",6379, 0);
			RedisMsgPubSubListener sp = new RedisMsgPubSubListener();
			jedis.subscribe(sp, "runoobChat");
		} catch (Exception e){
			e.printStackTrace();
		} finally {
			if (jedis != null){
				jedis.disconnect();
			}
		}

	}


}
