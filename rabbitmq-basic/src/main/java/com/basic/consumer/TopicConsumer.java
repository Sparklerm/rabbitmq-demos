package com.basic.consumer;

import com.basic.config.RabbitTopicConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class TopicConsumer {
    @RabbitListener(queues = RabbitTopicConfig.COLOR)
    public void color(String message) {
        System.out.println("Color Receiver : " + message);
    }

    @RabbitListener(queues = RabbitTopicConfig.COLOR_BLUE)
    public void blue(String message) {
        System.out.println("Blur Receiver : " + message);
    }

    @RabbitListener(queues = RabbitTopicConfig.COLOR_RED)
    public void red(String message) {
        System.out.println("Red Receiver : " + message);
    }
}
