package com.posada.santiago.alphapostsandcomments.domain.commands;

import co.com.sofka.domain.generic.Command;

public class UpdatePostTitleCommand extends Command {

    private String postId;
    private String title;

    public UpdatePostTitleCommand() {
    }

    public UpdatePostTitleCommand(String postId, String title) {
        this.postId = postId;
        this.title = title;
    }

    public String getPostId() {
        return postId;
    }

    public String getTitle() {
        return title;
    }
}
