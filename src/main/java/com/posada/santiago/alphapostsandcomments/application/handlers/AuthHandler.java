package com.posada.santiago.alphapostsandcomments.application.handlers;

import com.posada.santiago.alphapostsandcomments.application.generic.models.AppUser;
import com.posada.santiago.alphapostsandcomments.application.generic.models.AuthRequest;
import com.posada.santiago.alphapostsandcomments.application.services.CreateUserService;
import com.posada.santiago.alphapostsandcomments.application.services.LoginUserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class AuthHandler {

    @Bean
    RouterFunction<ServerResponse> createUser(CreateUserService createUserService){
        return route(POST("/auth/create/{role}"),
                request -> request.bodyToMono(AppUser.class)
                        .flatMap(user -> request.pathVariable("role").equals("admin")
                                                ? createUserService.createUser(user,"ROLE_ADMIN")
                                                : createUserService.createUser(user,"ROLE_USER"))
                        .flatMap(user -> ServerResponse.status(HttpStatus.CREATED).bodyValue(user)));

    }

    @Bean
    RouterFunction<ServerResponse> loginRouter(LoginUserService loginUserService){
        return route(POST("/auth/login"),
                request -> loginUserService.logIn(request.bodyToMono(AuthRequest.class)));
    }

}
