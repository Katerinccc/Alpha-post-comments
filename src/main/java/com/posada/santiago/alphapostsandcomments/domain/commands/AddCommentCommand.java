package com.posada.santiago.alphapostsandcomments.domain.commands;

import co.com.sofka.domain.generic.Command;

public class AddCommentCommand extends Command {

    private String postId;

    private String commentId;
    private String author;
    private String content;

    public AddCommentCommand() {
    }

    public AddCommentCommand(String postId, String commentId, String author, String content) {
        this.postId = postId;
        this.commentId = commentId;
        this.author = author;
        this.content = content;
    }

    public String getPostId() {
        return postId;
    }

    public String getCommentId() {
        return commentId;
    }

    public String getAuthor() {
        return author;
    }


    public String getContent() {
        return content;
    }

}
