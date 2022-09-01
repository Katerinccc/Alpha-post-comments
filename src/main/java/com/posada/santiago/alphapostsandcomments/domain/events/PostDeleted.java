package com.posada.santiago.alphapostsandcomments.domain.events;

import co.com.sofka.domain.generic.DomainEvent;

public class PostDeleted extends DomainEvent {

    private String postId;

    public PostDeleted() {
        super("posada.santiago.postdeleted");
    }

    public PostDeleted(String postId) {
        super("posada.santiago.postdeleted");
        this.postId = postId;
    }

    public String getPostId() {
        return postId;
    }
}
