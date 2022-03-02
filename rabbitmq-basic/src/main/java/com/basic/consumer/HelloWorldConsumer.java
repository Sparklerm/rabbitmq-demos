package com.basic.consumer;

import com.basic.config.RabbitHelloWorldConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class HelloWorldConsumer {

    @RabbitListener(queues = RabbitHelloWorldConfig.QUEUE_NAME)
    public void receive(String msg) {
        System.out.println(RabbitHelloWorldConfig.QUEUE_NAME + " : " + msg);
    }

}
