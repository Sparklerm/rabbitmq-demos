package com.basic.consumer;

import com.basic.config.RabbitWorkQueueConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class WorkQueueConsumer {
    @RabbitListener(queues = RabbitWorkQueueConfig.QUEUE_NAME)
    public void receive1(String msg) {
        System.out.println(RabbitWorkQueueConfig.QUEUE_NAME + " receive1 : " + msg);
    }

    /**
     * 第二个消费者配置了 concurrency 为 10，此时，对于第二个消费者，将会同时存在 10 个子线程去消费消息。
     *
     * @param msg
     */
    @RabbitListener(queues = RabbitWorkQueueConfig.QUEUE_NAME, concurrency = "10")
    public void receive2(String msg) {
        System.out.println(RabbitWorkQueueConfig.QUEUE_NAME + " receive2 : " + msg);
    }
}
