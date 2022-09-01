package com.posada.santiago.alphapostsandcomments.domain;

import co.com.sofka.domain.generic.AggregateEvent;
import co.com.sofka.domain.generic.DomainEvent;
import com.posada.santiago.alphapostsandcomments.domain.events.PostCreated;
import com.posada.santiago.alphapostsandcomments.domain.events.PostDeleted;
import com.posada.santiago.alphapostsandcomments.domain.events.PostTitleUpdated;
import com.posada.santiago.alphapostsandcomments.domain.values.*;
import com.posada.santiago.alphapostsandcomments.domain.events.CommentAdded;
import java.util.List;
import java.util.Objects;

public class Post extends AggregateEvent<PostId> {

    protected Title title;

    protected Author author;

    protected List<Comment> comments;

    public Post(PostId entityId, Title title, Author author) {
        super(entityId);
        subscribe(new PostChange(this));
        appendChange(new PostCreated(title.value(), author.value())).apply();
    }


    private Post(PostId id){
        super(id);
        subscribe(new PostChange(this));
    }

    public static Post from(PostId id, List<DomainEvent> events){
        Post post = new Post(id);
        events.forEach(event -> post.applyEvent(event));
        return post;
    }

    public void addAComment(CommentId id, Author author, Content content){
        Objects.requireNonNull(id);
        Objects.requireNonNull(author);
        Objects.requireNonNull(content);
        appendChange(new CommentAdded(id.value(), author.value(), content.value())).apply();
    }

    public void updatePostTitle(Title title){
        Objects.requireNonNull(title);
        appendChange(new PostTitleUpdated(title.value())).apply();
    }

//    public void deletePost(PostId id){
//        Objects.requireNonNull(id);
//        appendChange(new PostDeleted(id.value())).apply();
//    }

}
