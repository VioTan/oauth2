package com.oauth.serviceauth;

import com.oauth.serviceauth.customImpl.MyRedisTokenStore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ServiceAuthApplicationTests {

    @Resource
    private RedisTemplate<String,String> redisTemplate;

    @Resource
    RedisConnectionFactory redisConnectionFactory;
    @Test
    public void testRedis() {
        redisTemplate.opsForValue().set("myKey","myValue");
        System.out.println(redisTemplate.opsForValue().get("myKey"));
        System.out.println();
        new MyRedisTokenStore(redisConnectionFactory);
    }

}
