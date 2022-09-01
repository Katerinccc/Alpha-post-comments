package com.posada.santiago.alphapostsandcomments.business.generic;

import co.com.sofka.domain.generic.Command;
import co.com.sofka.domain.generic.DomainEvent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Function;

public abstract class UseCaseForCommandDelete<R extends Command> implements Function<String, Mono<Void>> {
    public abstract Mono<Void> apply(String id);
}
