package com.basic.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 工作队列
 */
@Configuration
public class RabbitWorkQueueConfig {

    public static final String QUEUE_NAME = "work_queue";

    @Bean
    Queue workQueue() {
        /**
         *  1. 第一个参数是队列的名字
         *  2. 第二个参数是持久化
         *  3. 该队列是否具有排他性，具有排他性的队列只能被创建其的 Connection 处理
         *  4. 如果该队列没有消费者，是否自动删除队列
         */
        return new Queue(QUEUE_NAME, true, false, false);
    }
}
