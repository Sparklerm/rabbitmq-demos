package com.dlx.consumer;

import com.dlx.config.RabbitDLXConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author Sparkler
 * @date 2022/3/2
 */
@Component
public class DLXConsumer {
    @RabbitListener(queues = RabbitDLXConfig.DLX_QUEUE_NAME)
    public void dlxConsumer(String message) {
        System.out.println(RabbitDLXConfig.DLX_QUEUE_NAME + "  ----->  " + message);
    }
}
