package com.hlllg.springbootinit.bizmq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author: HLLLG
 * @Date: 2024/04/20/10:40
 */
@Component
public class MyMessageProducer {
    @Resource
    private RabbitTemplate rabbitTemplate;

    public void sendMessage(String exchange, String routineKey, String message) {
        rabbitTemplate.convertAndSend(exchange, routineKey, message);
    }
}
