package jjk.csutils.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

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
        vot.set(key,value);
    }


    public void setKeyValTime(String key, String val, long time) {
        ValueOperations vot = this.redisTemplate.opsForValue();
        vot.set(key,val,time);
    }


    public String getVal(String key) {
        ValueOperations vot = this.redisTemplate.opsForValue();
        Object o = vot.get(key);
        return o.toString();
    }
}
