package com.dlx.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * 1. 首先配置一个消息队列，new 一个 Queue：第一个参数是消息队列的名字；第二个参数表示消息是否持久化；第三个参数表示消息队列是否排他，一般我们都是设置为 false，即不排他；第四个参数表示如果该队列没有任何订阅的消费者的话，该队列会被自动删除，一般适用于临时队列。
 * 2. 配置一个 DirectExchange 交换机。
 * 3. 将交换机和队列绑定到一起。
 *
 * @author Sparkler
 * @date 2022/3/2
 */
@Configuration
public class RabbitTTLConfig {
    public static final String QUEUE_NAME_1 = "ttl_queue_1";
    public static final String QUEUE_NAME_2 = "ttl_queue_2";
    public static final String EXCHANGE_NAME = "ttl_exchange";
    public static final String HELLO_ROUTING_KEY = "ttl_routing_key";

    @Bean
    Queue queue_1() {
        return new Queue(QUEUE_NAME_1, true, false, false);
    }

    /**
     * 方式二，在声明队列时设置队列消息的有效期。通过此方式创建的队列在控制面板可见TTL标识。
     *
     * @return
     */
    @Bean
    Queue queue_2() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-message-ttl", 10000);
        return new Queue(QUEUE_NAME_2, true, false, false, args);
    }

    @Bean
    DirectExchange directExchange() {
        return new DirectExchange(EXCHANGE_NAME, true, false);
    }

    @Bean
    Binding binding_1() {
        return BindingBuilder.bind(queue_1())
                .to(directExchange())
                .with(HELLO_ROUTING_KEY);
    }

    @Bean
    Binding binding_2() {
        return BindingBuilder.bind(queue_2())
                .to(directExchange())
                .with(HELLO_ROUTING_KEY);
    }
}
