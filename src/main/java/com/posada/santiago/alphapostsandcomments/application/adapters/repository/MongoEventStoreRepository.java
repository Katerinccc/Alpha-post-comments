package com.posada.santiago.alphapostsandcomments.application.adapters.repository;

import co.com.sofka.domain.generic.DomainEvent;
import com.google.gson.Gson;
import com.posada.santiago.alphapostsandcomments.application.generic.models.StoredEvent;
import com.posada.santiago.alphapostsandcomments.business.gateways.DomainEventRepository;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.Comparator;
import java.util.Date;

@Repository
public class MongoEventStoreRepository implements DomainEventRepository {

    private final ReactiveMongoTemplate template;

    private final Gson gson = new Gson();

    public MongoEventStoreRepository(ReactiveMongoTemplate template) {
        this.template = template;
    }

    @Override
    public Flux<DomainEvent> findById(String aggregateId) {
        var query = new Query(Criteria.where("aggregateRootId").is(aggregateId));
        return template.find(query, DocumentEventStored.class)
                .sort(Comparator.comparing(event -> event.getStoredEvent().getOccurredOn()))
                .map(storeEvent -> {
                    try {
                        return (DomainEvent) gson.fromJson(storeEvent.getStoredEvent().getEventBody(),
                                Class.forName(storeEvent.getStoredEvent().getTypeName()));
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                        throw new IllegalStateException("could not found domain event");
                    }
                });
    }

    @Override
    public Mono<DomainEvent> saveEvent(DomainEvent event) {
        DocumentEventStored eventStored = new DocumentEventStored();
        eventStored.setAggregateRootId(event.aggregateRootId());
        eventStored.setStoredEvent(new StoredEvent(gson.toJson(event), new Date(), event.getClass().getCanonicalName()));
        return template.save(eventStored)
                .map(storeEvent -> {
                    try {
                        return (DomainEvent) gson.fromJson(storeEvent.getStoredEvent().getEventBody(),
                                Class.forName(storeEvent.getStoredEvent().getTypeName()));
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                        throw new IllegalStateException("could not found domain event");
                    }
                });
    }

    @Override
    public Flux<DomainEvent> deleteEventsByAggregateId(String aggregateId) {
        Query query = Query.query(Criteria.where("aggregateRootId").is(aggregateId));
        return template.findAllAndRemove(query, DocumentEventStored.class)
                .map(documentEventStored -> {
                    try {
                        return (DomainEvent) gson.fromJson(documentEventStored.getStoredEvent().getEventBody(),
                                Class.forName(documentEventStored.getStoredEvent().getTypeName()));
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

}