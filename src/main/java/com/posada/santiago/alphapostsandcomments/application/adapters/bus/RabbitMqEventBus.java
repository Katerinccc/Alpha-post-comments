package com.posada.santiago.alphapostsandcomments.application.adapters.bus;

import co.com.sofka.domain.generic.DomainEvent;
import com.google.gson.Gson;
import com.posada.santiago.alphapostsandcomments.application.config.RabbitConfig;
import com.posada.santiago.alphapostsandcomments.business.gateways.EventBus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RabbitMqEventBus implements EventBus {
    private final RabbitTemplate rabbitTemplate;
    private final Gson gson = new Gson();

    public RabbitMqEventBus(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void publish(DomainEvent event) {
        var notification = new Notification(
                event.getClass().getTypeName(),
                gson.toJson(event)
        );

        log.info("Send publish message to rabbit: " + event.getClass().getTypeName());
        rabbitTemplate.convertAndSend(
                RabbitConfig.EXCHANGE, RabbitConfig.GENERAL_ROUTING_KEY, notification.serialize().getBytes()
        );
    }

    @Override
    public void publishError(Throwable errorEvent) {
        var event = new ErrorEvent(errorEvent.getClass().getTypeName(), errorEvent.getMessage());
        var notification = new Notification(
                event.getClass().getTypeName(),
                gson.toJson(event)
        );

        log.info("Send publish error message to rabbit: " + errorEvent.getClass().getTypeName());

        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE, RabbitConfig.GENERAL_ROUTING_KEY,
                notification.serialize().getBytes());
    }
}
