package com.posada.santiago.alphapostsandcomments.business.usecases;

import co.com.sofka.domain.generic.DomainEvent;
import com.posada.santiago.alphapostsandcomments.business.gateways.DomainEventRepository;
import com.posada.santiago.alphapostsandcomments.business.gateways.EventBus;
import com.posada.santiago.alphapostsandcomments.business.generic.UseCaseForCommand;
import com.posada.santiago.alphapostsandcomments.domain.Post;
import com.posada.santiago.alphapostsandcomments.domain.commands.CreatePostCommand;
import com.posada.santiago.alphapostsandcomments.domain.values.Author;
import com.posada.santiago.alphapostsandcomments.domain.values.PostId;
import com.posada.santiago.alphapostsandcomments.domain.values.Title;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class CreatePostUseCase extends UseCaseForCommand<CreatePostCommand> {
    private final DomainEventRepository repository;
    private final EventBus bus;

    public CreatePostUseCase(DomainEventRepository repository, EventBus bus) {
        this.repository = repository;
        this.bus = bus;
    }

    @Override
    public Flux<DomainEvent> apply(Mono<CreatePostCommand> createPostCommandMono) {
        log.info("Create post process initialized..");
        return createPostCommandMono.flatMapIterable(command -> {
            Post post = new Post(PostId.of(command.getPostId()),
                    new Title(command.getTitle()),
                    new Author(command.getAuthor())
            );
            return post.getUncommittedChanges();
        }).flatMap(event ->
                repository.saveEvent(event)
                        .switchIfEmpty(Mono.defer(() -> Mono.error(new Throwable("The post could not be created."))))
                        .thenReturn(event))
                .doOnNext(returnedEvent-> {
                   bus.publish(returnedEvent);
                   log.info(returnedEvent.toString());
                });
    }
}
