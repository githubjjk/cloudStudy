package jjk.csutils.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author: jjk
 * @create: 2020-01-07 10:00
 * @program: cloudStudy
 * @description: redis实现
 */
@Service
public class RedisService {

    @Autowired
    private RedisTemplate redisTemplate;


    public void setKeyVal(String key, String value) {
        ValueOperations vot = this.redisTemplate.opsForValue();
        vot.set(key, value);
    }


    public void setKeyValTime(String key, String val, long time) {
        ValueOperations vot = this.redisTemplate.opsForValue();
        vot.set(key, val);
        redisTemplate.expire(key, time, TimeUnit.SECONDS);
    }


    public String getVal(String key) {
        ValueOperations<String, String> vot = this.redisTemplate.opsForValue();
        String s = vot.get(key);
        if (null != s) {
            return s;
        }
        return "";
    }

    public boolean removeVal(String key){
        Boolean delete = this.redisTemplate.delete(key);
        return delete;
    }
}
