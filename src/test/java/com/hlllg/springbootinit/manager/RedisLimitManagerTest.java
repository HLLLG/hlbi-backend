package com.hlllg.springbootinit.manager;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Author: HLLLG
 * @Date: 2024/04/17/16:14
 */
@SpringBootTest
class RedisLimitManagerTest {

    @Resource
    private RedisLimitManager redisLimitManager;

    @Test
    void doRateLimit() {
//        String userId = "1";
//        for (int i = 0; i < 5; i++) {
//            redisLimitManager.doRateLimit(userId);
//            System.out.println("成功");
//        }
    }
}