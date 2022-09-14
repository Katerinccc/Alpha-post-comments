package com.posada.santiago.alphapostsandcomments.application.config;


import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    public static final String EXCHANGE = "core-posts";

    public static final String GENERAL_QUEUE = "events.general";

    public static final String GENERAL_ROUTING_KEY = "routingKey.general";

    @Bean
    public Queue generalQueue(){
        return new Queue(GENERAL_QUEUE);
    }

    @Bean
    public TopicExchange getTopicExchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Binding BindingToGeneralQueue() {
        return BindingBuilder.bind(generalQueue()).to(getTopicExchange()).with(GENERAL_ROUTING_KEY);
    }
}
