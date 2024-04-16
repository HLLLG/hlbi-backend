package com.hlllg.springbootinit.manager;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Author: HLLLG
 * @Date: 2024/04/16/11:45
 */
@SpringBootTest
class AiManagerTest {

    @Resource
    private AiManager aiManager;

    @Test
    void doChat() {
//        String chat = aiManager.doChat("华晨宇");
//        System.out.println(chat);
    }
}