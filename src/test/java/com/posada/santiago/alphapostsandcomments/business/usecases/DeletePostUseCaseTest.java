package com.posada.santiago.alphapostsandcomments.business.usecases;

import co.com.sofka.domain.generic.DomainEvent;
import com.posada.santiago.alphapostsandcomments.business.gateways.DomainEventRepository;
import com.posada.santiago.alphapostsandcomments.business.gateways.EventBus;
import com.posada.santiago.alphapostsandcomments.domain.commands.DeletePostCommand;
import com.posada.santiago.alphapostsandcomments.domain.events.PostDeleted;
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
class DeletePostUseCaseTest {

    @Mock
    DomainEventRepository repositoryMock;

    @Mock
    private EventBus eventBus;

    @InjectMocks
    DeletePostUseCase deletePostUseCase;

    @Test
    void deletePostUseCase(){

        var command = new DeletePostCommand("MpL13TK72Q");

        var domainEvent = new PostDeleted();
        domainEvent.setAggregateRootId(command.getPostId());

        BDDMockito.when(repositoryMock.deleteEventsByAggregateId(Mockito.any(String.class)))
                .thenReturn(Flux.just(domainEvent));

        BDDMockito.when(repositoryMock.saveEvent(Mockito.any(DomainEvent.class)))
                .thenReturn(Mono.just(domainEvent));

        var useCase = deletePostUseCase.apply(Mono.just(command));

        StepVerifier.create(useCase)
                .expectSubscription()
                .expectNextMatches(events -> events instanceof PostDeleted)
                .expectComplete()
                .verify();

        BDDMockito.verify(eventBus, BDDMockito.times(1))
                .publish(Mockito.any(DomainEvent.class));

        BDDMockito.verify(repositoryMock, BDDMockito.times(1))
                .deleteEventsByAggregateId(Mockito.any(String.class));

        BDDMockito.verify(repositoryMock, BDDMockito.times(1))
                .saveEvent(Mockito.any(DomainEvent.class));


    }


}