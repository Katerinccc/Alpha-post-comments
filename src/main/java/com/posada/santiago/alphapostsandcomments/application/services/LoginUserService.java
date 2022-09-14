package com.posada.santiago.alphapostsandcomments.application.services;

import com.posada.santiago.alphapostsandcomments.application.config.JwtTokenConfig;
import com.posada.santiago.alphapostsandcomments.application.generic.models.AuthRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import java.util.Map;

@AllArgsConstructor
@Service
public class LoginUserService {

    private final JwtTokenConfig jwtTokenConfig;
    private final ReactiveAuthenticationManager authenticationManager;

    public Mono<ServerResponse> logIn(Mono<AuthRequest> authRequest){
        return authRequest
                .flatMap(request -> this.authenticationManager
                        .authenticate(new UsernamePasswordAuthenticationToken(
                                request.getUserEmail(),
                                request.getPassword())
                        )
                        .onErrorMap(BadCredentialsException.class, err -> new Throwable(HttpStatus.FORBIDDEN.toString()))
                        .map(this.jwtTokenConfig::createToken))
                .flatMap(jwt-> {
                    var tokenBody = Map.of("access_token", jwt);
                    return ServerResponse
                            .ok()
                            .headers(httpHeaders1 -> httpHeaders1.add(HttpHeaders.AUTHORIZATION, "Bearer " + jwt))
                            .bodyValue(tokenBody);

                });
    }

}
