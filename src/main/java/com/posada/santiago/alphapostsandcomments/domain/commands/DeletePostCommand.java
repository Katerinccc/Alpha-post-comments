package com.posada.santiago.alphapostsandcomments.domain.commands;

import co.com.sofka.domain.generic.Command;

public class DeletePostCommand extends Command {

    private String postId;

    public DeletePostCommand() {
    }

    public DeletePostCommand(String postId) {
        this.postId = postId;
    }

    public String getPostId() {
        return postId;
    }
}
