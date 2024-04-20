package com.hlllg.springbootinit.mq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

public class FanoutConsumer {
  private static final String EXCHANGE_NAME = "fanout_exchange";

  public static void main(String[] argv) throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();
    Channel channel1 = connection.createChannel();
    // 创建交换机
//    channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
    // 绑定队列
    String queueName = "xiaowang";
    channel.queueDeclare(queueName,true, false, false, null);
    channel.queueBind(queueName, EXCHANGE_NAME, "");

    String queueName1 = "xiaoli";
    channel1.queueDeclare(queueName1,true, false, false, null);
    channel1.queueBind(queueName1, EXCHANGE_NAME, "");

    System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

    DeliverCallback deliverCallback = (consumerTag, delivery) -> {
        String message = new String(delivery.getBody(), "UTF-8");
        System.out.println(" [xiaowang] Received '" + message + "'");
    };
    DeliverCallback deliverCallback1 = (consumerTag, delivery) -> {
        String message = new String(delivery.getBody(), "UTF-8");
        System.out.println(" [xiaoli] Received '" + message + "'");
    };
    channel.basicConsume(queueName, true, deliverCallback, consumerTag -> { });
    channel1.basicConsume(queueName1, true, deliverCallback1, consumerTag -> { });
  }
}