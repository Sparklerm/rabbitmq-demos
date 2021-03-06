package com.basic.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * įŽåéå
 */
@Configuration
public class RabbitHelloWorldConfig {
    public static final String QUEUE_NAME = "hello_world_queue";

    @Bean
    Queue helloWorldQueue() {
        return new Queue(QUEUE_NAME);
    }

}
