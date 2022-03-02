package com.basic.consumer;

import com.basic.config.RabbitDirectConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class DirectConsumer {
    @RabbitListener(queues = RabbitDirectConfig.QUEUE_NAME)
    public void handler1(String msg) {
        System.out.println("DirectReceiver : " + msg);
    }
}
