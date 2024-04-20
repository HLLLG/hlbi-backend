package com.hlllg.springbootinit.mq;

import com.rabbitmq.client.*;

public class DirectConsumer {

  private static final String EXCHANGE_NAME = "direct_exchange";

  public static void main(String[] argv) throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();

    channel.exchangeDeclare(EXCHANGE_NAME, "direct");
    String queueName = "xiaoliang_queue";
    channel.queueDeclare(queueName, true, false, false, null);
    channel.queueBind(queueName, EXCHANGE_NAME, "xiaoliang");
    String queueName1 = "xiaohe_queue";
    channel.queueDeclare(queueName1, true, false, false, null);
    channel.queueBind(queueName1, EXCHANGE_NAME, "xiaohe");

    System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

    DeliverCallback deliverCallback = (consumerTag, delivery) -> {
        String message = new String(delivery.getBody(), "UTF-8");
        System.out.println(" [xiaoliang] Received '" +
            delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
    };
    DeliverCallback deliverCallback1 = (consumerTag, delivery) -> {
        String message = new String(delivery.getBody(), "UTF-8");
        System.out.println(" [xiaohe] Received '" +
            delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
    };
    channel.basicConsume(queueName, true, deliverCallback, consumerTag -> { });
    channel.basicConsume(queueName1, true, deliverCallback1, consumerTag -> { });
  }
}