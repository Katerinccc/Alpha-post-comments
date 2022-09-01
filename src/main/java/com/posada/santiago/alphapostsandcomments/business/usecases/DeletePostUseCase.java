package com.posada.santiago.alphapostsandcomments.business.usecases;

import co.com.sofka.domain.generic.DomainEvent;
import com.posada.santiago.alphapostsandcomments.business.gateways.DomainEventRepository;
import com.posada.santiago.alphapostsandcomments.business.gateways.EventBus;
import com.posada.santiago.alphapostsandcomments.business.generic.UseCaseForCommand;
import com.posada.santiago.alphapostsandcomments.business.generic.UseCaseForCommandDelete;
import com.posada.santiago.alphapostsandcomments.domain.Post;
import com.posada.santiago.alphapostsandcomments.domain.commands.DeletePostCommand;
import com.posada.santiago.alphapostsandcomments.domain.values.Author;
import com.posada.santiago.alphapostsandcomments.domain.values.PostId;
import com.posada.santiago.alphapostsandcomments.domain.values.Title;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.function.Function;

@Component
public class DeletePostUseCase extends UseCaseForCommandDelete<DeletePostCommand>
//extends Command implements UseCaseForCommand<Mono<DeletePostCommand>, Flux<DomainEvent>>
 {

    private final DomainEventRepository repository;
    private final EventBus bus;

    public DeletePostUseCase(DomainEventRepository repository, EventBus bus) {
        this.repository = repository;
        this.bus = bus;
    }


    @Override
    public Mono<Void> apply(String id) {
        return repository.deleteEventsByAggregateId(id);

//        return deletePostCommandMono.flatMapMany(command -> repository.deleteEventsByAggregateId(command.getPostId())
//                .collectList()
//                .flatMapIterable(events -> {
//                    Post post = Post.from(PostId.of(command.getPostId()), events);
//                    post.deletePost(PostId.of(command.getPostId()));
//                    return post.getUncommittedChanges();
//                }).map(event -> {
//                    bus.publish(event);
//                    return event;
//                }).flatMap(event -> repository.saveEvent(event))
//        );

    }
}
