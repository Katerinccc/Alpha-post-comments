package com.posada.santiago.alphapostsandcomments.business.usecases;


import co.com.sofka.domain.generic.DomainEvent;
import com.posada.santiago.alphapostsandcomments.business.gateways.DomainEventRepository;
import com.posada.santiago.alphapostsandcomments.business.gateways.EventBus;
import com.posada.santiago.alphapostsandcomments.domain.commands.CreatePostCommand;
import com.posada.santiago.alphapostsandcomments.domain.events.PostCreated;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class CreatePostUseCaseTest {

    @Mock
    DomainEventRepository repositoryMock;

    @Mock
    private EventBus eventBus;

    CreatePostUseCase createPostUseCase;

    @BeforeEach
    void init(){
        createPostUseCase = new CreatePostUseCase(repositoryMock, eventBus);
    }

    @Test
    void createPostUseCaseTest(){

        var command = new CreatePostCommand("MdPL11Kt13",
                "Katerin Calderon",
                "My fist test");

        var domainEvent = new PostCreated(command.getAuthor(),
                command.getTitle());
        domainEvent.setAggregateRootId(command.getPostId());

        BDDMockito.when(repositoryMock.saveEvent(Mockito.any(DomainEvent.class)))
                .thenReturn(Mono.just(domainEvent));

        var useCase = createPostUseCase.apply(Mono.just(command));

        StepVerifier.create(useCase)
                .expectSubscription()
                .expectNextMatches(events -> events instanceof PostCreated)
                .expectComplete()
                .verify();

    }

}