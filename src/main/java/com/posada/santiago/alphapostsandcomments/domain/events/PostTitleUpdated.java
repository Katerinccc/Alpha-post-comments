package com.posada.santiago.alphapostsandcomments.domain.events;

import co.com.sofka.domain.generic.DomainEvent;

public class PostTitleUpdated extends DomainEvent {

    private String title;

    public PostTitleUpdated() {
        super("posada.santiago.postTitleUpdated");
    }

    public PostTitleUpdated(String title) {
        super("posada.santiago.postTitleUpdated");
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
