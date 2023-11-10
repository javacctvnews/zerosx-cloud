package com.zerosx.order.test;

import com.zerosx.common.core.utils.IdGenerator;
import com.zerosx.common.redis.templete.RedissonOpService;
import com.zerosx.order.OrderApplication;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.redisson.client.protocol.ScoredEntry;
import org.redisson.codec.SerializationCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

/**
 * RedisDemoTest
 * <p>
 *
 * @author: javacctvnews
 * @create: 2023-09-17 14:23
 **/
@Slf4j
@SpringBootTest(classes = OrderApplication.class)
public class RedisDemoTest {

    @Autowired
    private RedissonOpService redissonOpService;
    @Autowired
    private SerializationCodec serializationCodec;


    @Test
    public void testSortedSet(){
        redissonOpService.zAdd("key01", 100, 12.0, 300, serializationCodec);
        redissonOpService.zAdd("key01", 200, 13.0, 300, serializationCodec);
        redissonOpService.zAdd("key01", 300, 14.0, 300, serializationCodec);

        boolean b = redissonOpService.zRem("key01", 100, serializationCodec);
        System.out.println("b = " + b);

    }

    @Test
    public void testString() {
        redissonOpService.set("test01", "1234567890");
        long test01Expire = redissonOpService.ttl("test01");
        log.debug("test01过期时间:{}", test01Expire);
        redissonOpService.set("test02", "1234567890", 30);
        long test02Expire = redissonOpService.ttl("test02");
        log.debug("test02过期时间:{}", test02Expire);
        String test01 = redissonOpService.getAndDelete("test01");
        log.debug("test01的内容:{}", test01);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class A {
        private String name;
        private Integer age;
    }

    @Test
    public void testDel() {
        boolean del = redissonOpService.hDel("hash01");
        log.debug("删除hash01的内容:{}", del);
    }

    @Test
    public void testHash() throws Exception {
        String key = "hash01";
        redissonOpService.hPut(key, "field1", IdGenerator.getIdStr());
        redissonOpService.hPut(key, "field2", IdGenerator.getIdStr());
        redissonOpService.hPut(key, "field3", IdGenerator.getIdStr());
        redissonOpService.hPut(key, "field4", IdGenerator.getIdStr());

        Map<String, Object> fieMap = new HashMap<>();
        fieMap.put("field91", IdGenerator.getIdStr());
        fieMap.put("field92", IdGenerator.getIdStr());
        fieMap.put("field93", IdGenerator.getIdStr());
        fieMap.put("field94", IdGenerator.getIdStr());
        redissonOpService.hPut(key, fieMap);

        Map<String, A> fieMapA = new HashMap<>();
        fieMapA.put("obj1", new A("zhangsan", 10));
        fieMapA.put("obj2", new A("lisi", 12));
        fieMapA.put("obj3", new A("wangwu", 30));
        redissonOpService.hPut(key, fieMapA);

        Map<String, Object> objectMap = redissonOpService.hGet(key);
        objectMap.forEach((key1, value) -> {
            log.debug("{}={}", key1, value);
        });

        Set<String> hashKeys = new HashSet<>();
        hashKeys.add("obj1");
        hashKeys.add("obj2");
        Map<String, A> objectMap1 = redissonOpService.hGet(key, hashKeys);
        objectMap1.forEach((key1, value) -> {
            log.debug("{}={}", key1, value);
        });

        Thread.sleep(120 * 1000);

        boolean del = redissonOpService.hDel(key);
        log.debug("删除hash01的内容:{}", del);
    }


    @Test
    public void testScoredSet() {
        String key = "D001";
        Map<String, Double> map = new HashMap<>();
        map.put("1", 1D);
        map.put("12", 2D);
        map.put("122", 4D);
        map.put("1222", 5D);
        map.put("12222", 6D);
        map.put("122222", 7D);
        map.put("1222222", 8D);
        map.put("12222222", 9D);
        map.put("122222222", 10D);
        map.put("1222222222", 11D);
        map.put("12222222222", 12D);
        map.put("122222222222", 13D);
        map.put("1222222222222", 14D);
        map.put("12222222222222", 15D);
        map.put("122222222222222", 16D);

        redissonOpService.zAdd(key, map);
        Double updateScore = redissonOpService.zAddScore(key, "122222222222222", 10D);
        int count = redissonOpService.zLen(key);
        log.debug("{} 元素个数:{}", key, count);
        Collection<ScoredEntry<String>> collection = redissonOpService.zEntryRange(key, 0, 10);
        collection.forEach(e -> log.debug("{}={}", e.getValue(), e.getScore()));

    }


}
