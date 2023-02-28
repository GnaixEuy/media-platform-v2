package cn.GnaixEuy.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * <img src="http://blog.gnaixeuy.cn/wp-content/uploads/2022/09/倒闭.png"/>
 *
 * <p>项目： media-v2 </p>
 * 创建日期： 2023/2/28
 *
 * @author GnaixEuy
 * @version 1.0.0
 * @see <a href="https://github.com/GnaixEuy"> GnaixEuy的GitHub </a>
 */
@Component
public class RedisUtils {

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 查询key，支持模糊查询
     *
     * @param key
     */
    public Set<String> keys(String key) {
        return this.redisTemplate.keys(key);
    }

    /**
     * 获取值
     *
     * @param key
     */
    public String get(String key) {
        return this.redisTemplate.opsForValue().get(key);
    }

    /**
     * 设置值
     *
     * @param key
     * @param value
     */
    public void set(String key, String value) {
        this.redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 设置值，并设置过期时间
     *
     * @param key
     * @param value
     * @param expire 过期时间，单位秒
     */
    public void set(String key, String value, Integer expire) {
        this.redisTemplate.opsForValue().set(key, value, expire, TimeUnit.SECONDS);
    }

    /**
     * 删出key
     *
     * @param key
     */
    public void delete(String key) {
        this.redisTemplate.opsForValue().getOperations().delete(key);
    }

    /**
     * 实现命令：DEL key，删除一个key
     *
     * @param key
     */
    public void del(String key) {
        this.redisTemplate.delete(key);
    }

    /**
     * 设置对象
     *
     * @param key     key
     * @param hashKey hashKey
     * @param object  对象
     */
    public void hset(String key, String hashKey, Object object) {
        this.redisTemplate.opsForHash().put(key, hashKey, object);
    }

    /**
     * 设置对象
     *
     * @param key     key
     * @param hashKey hashKey
     * @param object  对象
     * @param expire  过期时间，单位秒
     */
    public void hset(String key, String hashKey, Object object, Integer expire) {
        this.redisTemplate.opsForHash().put(key, hashKey, object);
        this.redisTemplate.expire(key, expire, TimeUnit.SECONDS);
    }

    /**
     * 设置HashMap
     *
     * @param key key
     * @param map map值
     */
    public void hset(String key, HashMap<String, Object> map) {
        this.redisTemplate.opsForHash().putAll(key, map);
    }

    /**
     * key不存在时设置值
     *
     * @param key
     * @param hashKey
     * @param object
     */
    public void hsetAbsent(String key, String hashKey, Object object) {
        this.redisTemplate.opsForHash().putIfAbsent(key, hashKey, object);
    }

    /**
     * 获取Hash值
     *
     * @param key
     * @param hashKey
     * @return
     */
    public Object hget(String key, String hashKey) {
        return this.redisTemplate.opsForHash().get(key, hashKey);
    }

    /**
     * 获取key的所有值
     *
     * @param key
     * @return
     */
    public Object hget(String key) {
        return this.redisTemplate.opsForHash().entries(key);
    }

    /**
     * 删除key的所有值
     *
     * @param key
     */
    public void deleteKey(String key) {
        this.redisTemplate.opsForHash().getOperations().delete(key);
    }

    /**
     * 判断key下是否有值
     *
     * @param key
     */
    public Boolean hasKey(String key) {
        return this.redisTemplate.opsForHash().getOperations().hasKey(key);
    }


    /**
     * 实现命令：decrement key，减少key一次
     *
     * @param key
     * @return
     */
    public long decrement(String key, long delta) {
        return redisTemplate.opsForValue().decrement(key, delta);
    }


    /**
     * 判断key和hasKey下是否有值
     *
     * @param key
     * @param hasKey
     */
    public Boolean hasKey(String key, String hasKey) {
        return this.redisTemplate.opsForHash().hasKey(key, hasKey);
    }


    /**
     * 实现命令：increment key，增加key一次
     *
     * @param key
     * @return
     */
    public long increment(String key, long delta) {
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 判断key是否存在
     *
     * @param key
     * @return
     */
    public boolean keyIsExist(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 如果key不存在，则设置，如果存在，则报错
     *
     * @param key
     * @param value
     */
    public void setnx60s(String key, String value) {
        redisTemplate.opsForValue().setIfAbsent(key, value, 60, TimeUnit.SECONDS);
    }

}