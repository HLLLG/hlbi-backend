package com.hlllg.springbootinit.mq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.util.HashMap;
import java.util.Map;

public class DlxDirectConsumer {

  private static final String DEAD_EXCHANGE_NAME = "dlx_direct_exchange";

  private static final String EXCHANGE_NAME = "direct2_exchange";

  public static void main(String[] argv) throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();
    channel.exchangeDeclare(EXCHANGE_NAME, "direct");

    channel.exchangeDeclare("some.exchange.name", "direct");

    // 指定死信队列参数
    Map<String, Object> args = new HashMap<>();
    args.put("x-dead-letter-exchange", DEAD_EXCHANGE_NAME);
    // 指定死信交换机发送到哪个死信队列
    args.put("x-dead-letter-routing-key", "waibao");

    Map<String, Object> args1 = new HashMap<>();
    args1.put("x-dead-letter-exchange", DEAD_EXCHANGE_NAME);
    args1.put("x-dead-letter-routing-key", "laoban");

    String queueName = "xiaomao_queue";
    channel.queueDeclare(queueName, true, false, false, args);
    channel.queueBind(queueName, EXCHANGE_NAME, "xiaomao");
    String queueName1 = "xiaogou_queue";
    channel.queueDeclare(queueName1, true, false, false, args1);
    channel.queueBind(queueName1, EXCHANGE_NAME, "xiaogou");

    System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

    DeliverCallback deliverCallback = (consumerTag, delivery) -> {
        String message = new String(delivery.getBody(), "UTF-8");
        // 拒绝消息
        channel.basicNack(delivery.getEnvelope().getDeliveryTag(), false, false);
        System.out.println(" [xiaomao] Received '" +
            delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
    };
    DeliverCallback deliverCallback1 = (consumerTag, delivery) -> {
        String message = new String(delivery.getBody(), "UTF-8");
      // 拒绝消息
      channel.basicNack(delivery.getEnvelope().getDeliveryTag(), false, false);
        System.out.println(" [xiaogou] Received '" +
            delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
    };
    channel.basicConsume(queueName, false, deliverCallback, consumerTag -> { });
    channel.basicConsume(queueName1, false, deliverCallback1, consumerTag -> { });

    String laobanqueueName = "laoban_dlx_queue";
    String waibaoqueueName2 = "waibao_dlx_queue";

    DeliverCallback laobandeliverCallback = (consumerTag, delivery) -> {
      String message = new String(delivery.getBody(), "UTF-8");
      System.out.println(" [laoban] Received '" +
              delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
    };
    DeliverCallback waibaodeliverCallback1 = (consumerTag, delivery) -> {
      String message = new String(delivery.getBody(), "UTF-8");
      System.out.println(" [waibao] Received '" +
              delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
    };
    channel.basicConsume(laobanqueueName, false, laobandeliverCallback, consumerTag -> { });
    channel.basicConsume(waibaoqueueName2, false, waibaodeliverCallback1, consumerTag -> { });
  }
}