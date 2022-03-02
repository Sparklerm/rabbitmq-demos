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
 * @author Sparkler
 * @date 2022/3/2
 */
@Configuration
public class RabbitDLXConfig {
    public static final String DLX_EXCHANGE_NAME = "dlx_exchange";
    public static final String DLX_QUEUE_NAME = "dlx_queue";
    public static final String DLX_ROUTING_KEY = "dlx_routing_key";
    public static final String TTL_QUEUE_NAME = "ttl_dlx_queue";

    /**
     * 配置死信交换机
     *
     * @return
     */
    @Bean
    DirectExchange dlxDirectExchange() {
        return new DirectExchange(DLX_EXCHANGE_NAME, true, false);
    }

    /**
     * 配置死信队列
     *
     * @return
     */
    @Bean
    Queue dlxQueue() {
        return new Queue(DLX_QUEUE_NAME);
    }

    /**
     * 绑定死信队列和死信交换机
     *
     * @return
     */
    @Bean
    Binding dlxBinding() {
        return BindingBuilder.bind(dlxQueue())
                .to(dlxDirectExchange())
                .with(DLX_ROUTING_KEY);
    }

    /**
     * 为消息队列配置死信交换机
     * x-dead-letter-exchange：配置死信交换机。
     * x-dead-letter-routing-key：配置死信 `routing_key`。
     * 发送到这个消息队列上的消息，如果发生了 nack、reject 或者过期等问题，就会被发送到 DLX 上，进而进入到与 DLX 绑定的消息队列上。
     *
     * @return
     */
    @Bean
    Queue queue() {
        Map<String, Object> args = new HashMap<>();
        //设置消息过期时间
        args.put("x-message-ttl", 0);
        //设置死信交换机
        args.put("x-dead-letter-exchange", DLX_EXCHANGE_NAME);
        //设置死信 routing_key
        args.put("x-dead-letter-routing-key", DLX_ROUTING_KEY);
        return new Queue(TTL_QUEUE_NAME, true, false, false, args);
    }
}
