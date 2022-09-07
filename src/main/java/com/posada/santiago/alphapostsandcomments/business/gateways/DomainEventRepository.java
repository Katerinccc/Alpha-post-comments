package com.posada.santiago.alphapostsandcomments.business.gateways;

import co.com.sofka.domain.generic.DomainEvent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface DomainEventRepository {
    Flux<DomainEvent> findById(String aggregateId);
    Mono<DomainEvent> saveEvent(DomainEvent event);

    Flux<DomainEvent> deleteEventsByAggregateId(String aggregateId);


}
