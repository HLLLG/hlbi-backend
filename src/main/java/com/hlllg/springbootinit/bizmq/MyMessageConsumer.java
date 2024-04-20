package com.hlllg.springbootinit.bizmq;

import com.rabbitmq.client.Channel;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author: HLLLG
 * @Date: 2024/04/20/10:40
 */
@Component
@Slf4j
public class MyMessageConsumer {
    @Resource
    private RabbitTemplate rabbitTemplate;

    // 指定程序监听的消息队列和确认方式
    @SneakyThrows
    @RabbitListener(queues = {"code_queue"}, ackMode = "MANUAL")
    public void receiveMessage(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliverTag) {
       log.info("receiveMessage message = {}", message);
       channel.basicAck(deliverTag, false);
    }
}
