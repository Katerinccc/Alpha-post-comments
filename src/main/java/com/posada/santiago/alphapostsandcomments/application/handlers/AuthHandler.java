package com.posada.santiago.alphapostsandcomments.application.handlers;

import com.posada.santiago.alphapostsandcomments.application.generic.models.AppUser;
import com.posada.santiago.alphapostsandcomments.application.generic.models.AuthRequest;
import com.posada.santiago.alphapostsandcomments.application.services.CreateUserService;
import com.posada.santiago.alphapostsandcomments.application.services.LoginUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.WebFilter;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Slf4j
@Configuration
public class AuthHandler {

    @Bean
    RouterFunction<ServerResponse> createUser(CreateUserService createUserService){
        return route(POST("/auth/create/{role}"),
                request -> request.bodyToMono(AppUser.class)
                        .flatMap(user -> request.pathVariable("role").equals("admin")
                                                ? createUserService.createUser(user,"ROLE_ADMIN")
                                                : createUserService.createUser(user,"ROLE_USER"))
                        .flatMap(user -> ServerResponse.status(HttpStatus.CREATED).bodyValue(user))
                        .onErrorResume(error -> {
                            log.error(error.getMessage());
                            return ServerResponse.badRequest().build();
                        })
        );

    }

    @Bean
    RouterFunction<ServerResponse> loginRouter(LoginUserService loginUserService){
        return route(POST("/auth/login"),
                request -> loginUserService.logIn(request.bodyToMono(AuthRequest.class))
                        .flatMap(tokenResponse ->
                                ServerResponse.ok()
                                .headers(httpHeaders1 ->
                                        httpHeaders1.add(HttpHeaders.AUTHORIZATION,
                                                "Bearer " + tokenResponse.getAccess_token()))
                                .bodyValue(tokenResponse)
                        .onErrorResume(error -> {
                            log.error(error.getMessage());
                            return ServerResponse.badRequest().build();
                        })));
    }

    @Bean
    WebFilter badCredentialsException() {
        return (exchange, next) -> next.filter(exchange)
                .onErrorResume(BadCredentialsException.class, error -> {
                    ServerHttpResponse response = exchange.getResponse();
                    response.setStatusCode(HttpStatus.FORBIDDEN);
                    log.error(error.getMessage());
                    return response.setComplete();
                });
    }

}


