package com.basic.consumer;

import com.basic.config.RabbitFanoutConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class FanoutConsumer {
    @RabbitListener(queues = RabbitFanoutConfig.QUEUE_NAME_1)
    public void receiver1(String message) {
        System.out.println("FanoutReceiver 1 : " + message);
    }

    @RabbitListener(queues = RabbitFanoutConfig.QUEUE_NAME_2)
    public void receiver2(String message) {
        System.out.println("FanoutReceiver 2 : " + message);
    }
}
