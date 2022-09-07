package com.posada.santiago.alphapostsandcomments.business.usecases;

import co.com.sofka.domain.generic.DomainEvent;
import com.posada.santiago.alphapostsandcomments.business.gateways.DomainEventRepository;
import com.posada.santiago.alphapostsandcomments.business.gateways.EventBus;
import com.posada.santiago.alphapostsandcomments.domain.commands.AddCommentCommand;
import com.posada.santiago.alphapostsandcomments.domain.events.CommentAdded;
import com.posada.santiago.alphapostsandcomments.domain.events.PostCreated;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class AddCommentUseCaseTest {

    @Mock
    DomainEventRepository repositoryMock;

    @Mock
    private EventBus eventBus;

    @InjectMocks
    AddCommentUseCase addCommentUseCase;

    @Test
    void addCommentToPostUseCase(){

        var command = new AddCommentCommand("11PK32Lm08",
                "98741piuBN86",
                "Katerin Calderon",
                "My first comment to test");

        var domainEvent = new CommentAdded(command.getCommentId(),
                            command.getAuthor(),
                            command.getContent());
        domainEvent.setAggregateRootId(command.getPostId());

        var postCreated = new PostCreated("New post to test", "Stephany Yepes");
        postCreated.setAggregateRootId(command.getPostId());

        BDDMockito.when(repositoryMock.findById(Mockito.any(String.class)))
                .thenReturn(Flux.just(postCreated));

        BDDMockito.when(repositoryMock.saveEvent(Mockito.any(DomainEvent.class)))
                .thenReturn(Mono.just(domainEvent));

        var useCase = addCommentUseCase.apply(Mono.just(command));

        StepVerifier.create(useCase)
                .expectSubscription()
                .expectNextMatches(events -> events instanceof CommentAdded)
                .expectComplete()
                .verify();

        BDDMockito.verify(eventBus, BDDMockito.times(1))
                .publish(Mockito.any(DomainEvent.class));

        BDDMockito.verify(repositoryMock, BDDMockito.times(1))
                .saveEvent(Mockito.any(DomainEvent.class));

        BDDMockito.verify(repositoryMock, BDDMockito.times(1))
                .findById(Mockito.any(String.class));

    }

}