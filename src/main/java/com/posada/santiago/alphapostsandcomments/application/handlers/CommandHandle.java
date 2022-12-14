package com.posada.santiago.alphapostsandcomments.application.handlers;

import com.posada.santiago.alphapostsandcomments.business.usecases.AddCommentUseCase;
import com.posada.santiago.alphapostsandcomments.business.usecases.CreatePostUseCase;
import com.posada.santiago.alphapostsandcomments.business.usecases.DeletePostUseCase;
import com.posada.santiago.alphapostsandcomments.business.usecases.UpdatePostTitleUseCase;
import com.posada.santiago.alphapostsandcomments.domain.commands.AddCommentCommand;
import com.posada.santiago.alphapostsandcomments.domain.commands.CreatePostCommand;
import com.posada.santiago.alphapostsandcomments.domain.commands.DeletePostCommand;
import com.posada.santiago.alphapostsandcomments.domain.commands.UpdatePostTitleCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.util.Arrays;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.PATCH;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@Slf4j
public class CommandHandle {

    @Bean
    public RouterFunction<ServerResponse> createPost(CreatePostUseCase useCase){
        return route(
                POST("/create/post").and(accept(MediaType.APPLICATION_JSON)),
                request -> useCase.apply(request.bodyToMono(CreatePostCommand.class))
                        .collectList()
                        .flatMap(domainEvents -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(domainEvents))
                        .onErrorResume(error -> {
                            log.error(error.getMessage());
                            log.error(Arrays.toString(error.getStackTrace()));
                            return ServerResponse.badRequest().build();
                        })
        );
    }

    @Bean
    public RouterFunction<ServerResponse> addComment(AddCommentUseCase useCase){

        return route(
                POST("/add/comment").and(accept(MediaType.APPLICATION_JSON)),
                request -> useCase.apply(request.bodyToMono(AddCommentCommand.class))
                        .collectList()
                        .flatMap(domainEvents -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(domainEvents))
                        .onErrorResume(error -> {
                            log.error(error.getMessage());
                            return ServerResponse.badRequest().build();
                        })
        );
    }

    @Bean
    public RouterFunction<ServerResponse> updatePostTitle(UpdatePostTitleUseCase useCase){

        return route(
                PATCH("/post/title").and(accept(MediaType.APPLICATION_JSON)),
                request -> useCase.apply(request.bodyToMono(UpdatePostTitleCommand.class))
                        .collectList()
                        .flatMap(domainEvents -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(domainEvents))
                        .onErrorResume(error -> {
                            log.error(error.getMessage());
                            return ServerResponse.badRequest().build();
                        })
        );
    }

    @Bean
    public RouterFunction<ServerResponse> deletePost(DeletePostUseCase useCase) {
        return route(
                DELETE("/delete/post").and(accept(MediaType.APPLICATION_JSON)),
                request -> useCase.apply(request.bodyToMono(DeletePostCommand.class))
                        .collectList()
                        .flatMap(domainEvents -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(domainEvents))
                        .onErrorResume(error -> {
                            log.error(error.getMessage());
                            return ServerResponse.badRequest().build();
                        })
        );
    }
}
