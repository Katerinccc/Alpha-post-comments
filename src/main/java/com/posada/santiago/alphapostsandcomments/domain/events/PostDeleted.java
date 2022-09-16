package com.posada.santiago.alphapostsandcomments.domain.events;

import co.com.sofka.domain.generic.DomainEvent;

public class PostDeleted extends DomainEvent {

    public PostDeleted() {
        super("posada.santiago.postdeleted");
    }

    @Override
    public String toString() {
        return "PostDeleted{" +
                "id= '" + aggregateRootId() +
                '}';
    }
}
