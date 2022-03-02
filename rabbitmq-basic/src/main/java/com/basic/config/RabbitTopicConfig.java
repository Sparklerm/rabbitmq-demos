package com.basic.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitTopicConfig {
    public final static String TOPIC_EXCHANGE = "topic_exchange_color";

    public static final String COLOR_BLUE = "color_blue";
    public static final String COLOR_BLUE_ROUTING_KEY = COLOR_BLUE + ".#";
    public static final String COLOR_RED = "color_red";
    public static final String COLOR_RED_ROUTING_KEY = COLOR_RED + ".#";
    public static final String COLOR = "color";
    public static final String COLOR_ROUTING_KEY = "#." + COLOR + ".#";

    @Bean
    TopicExchange topicExchange() {
        return new TopicExchange(TOPIC_EXCHANGE, true, false);
    }

    @Bean
    Queue blue() {
        return new Queue(COLOR_BLUE);
    }

    @Bean
    Queue red() {
        return new Queue(COLOR_RED);
    }

    @Bean
    Queue color() {
        return new Queue(COLOR);
    }

    @Bean
    Binding blueBinding() {
        return BindingBuilder.bind(blue()).to(topicExchange()).with(COLOR_BLUE_ROUTING_KEY);
    }

    @Bean
    Binding redBinding() {
        return BindingBuilder.bind(red()).to(topicExchange()).with(COLOR_RED_ROUTING_KEY);
    }

    @Bean
    Binding colorBinding() {
        return BindingBuilder.bind(color()).to(topicExchange()).with(COLOR_ROUTING_KEY);
    }
}
