package utils;

import comet.MessageQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by mengshuai on 2017/10/15.
 */
public final class RedisUtil {
    @Autowired private static RedisTemplate<String, Object> redisTemplate;

    public static void remove(final String key) {
        if (exists(key)) {
            redisTemplate.delete(key);
        }
    }

    public static boolean exists(final String key) {
        return redisTemplate.hasKey(key);
    }

    public Object get(final String key) {
        Object result = null;
        ValueOperations<String, Object> operations = redisTemplate
                .opsForValue();
        result = operations.get(key);
        return result;
    }

    public static boolean set(final String key, Object value) {
        boolean result = false;
        try {
            ValueOperations<String, Object> operations = redisTemplate
                    .opsForValue();
            operations.set(key, value);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static boolean set(final String key, Object value, Long expireTime) {
        boolean result = false;
        try {
            ValueOperations<String, Object> operations = redisTemplate
                    .opsForValue();
            operations.set(key, value);
            redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static boolean lpush(final String key, Object message) {
        boolean result = false;
        try {
            BoundListOperations<String, Object> operations = redisTemplate
                    .boundListOps(key);
            operations.leftPush(message);
            result = true;
            if (operations.size() > 0) {//TODO performance
                MessageQueue m = MessageQueue.getSingleInstance();
                synchronized (m) {
                    m.notify();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static Object rpop(final String key) {
        BoundListOperations<String, Object> boundListOperations =
                redisTemplate.boundListOps(key);
        return boundListOperations.rightPop();
    }

    public static List<Object> lrange(final String key, long start, long end) {
        BoundListOperations<String, Object> boundListOperations =
                redisTemplate.boundListOps(key);
        return boundListOperations.range(start, end);
    }

    public static boolean ltrim(final String key, long start, long end) {
        boolean result = false;
        try {
            BoundListOperations<String, Object> boundListOperations =
                    redisTemplate.boundListOps(key);
            boundListOperations.trim(start, end);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public void setRedisTemplate(
            RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
}