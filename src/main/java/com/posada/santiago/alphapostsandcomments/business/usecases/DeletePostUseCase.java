package com.posada.santiago.alphapostsandcomments.business.usecases;

import co.com.sofka.domain.generic.DomainEvent;
import com.posada.santiago.alphapostsandcomments.business.gateways.DomainEventRepository;
import com.posada.santiago.alphapostsandcomments.business.gateways.EventBus;
import com.posada.santiago.alphapostsandcomments.domain.commands.DeletePostCommand;
import com.posada.santiago.alphapostsandcomments.domain.events.PostDeleted;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.function.Function;

@Component
public class DeletePostUseCase implements Function<Mono<DeletePostCommand>, Flux<DomainEvent>>
 {

    private final DomainEventRepository repository;
    private final EventBus bus;

    public DeletePostUseCase(DomainEventRepository repository, EventBus bus) {
        this.repository = repository;
        this.bus = bus;
    }


    @Override
    public Flux<DomainEvent> apply(Mono<DeletePostCommand> deletePostCommand) {
        return deletePostCommand.flatMapMany(command-> repository.deleteEventsByAggregateId(command.getPostId()))
                .collectList()
                .flatMapMany(events -> {
                        PostDeleted postDeleted = new PostDeleted();
                        postDeleted.setAggregateRootId(events.get(0).aggregateRootId());
                        return repository.saveEvent(postDeleted);
                }).doOnNext(event -> bus.publish(event));
    }
}
