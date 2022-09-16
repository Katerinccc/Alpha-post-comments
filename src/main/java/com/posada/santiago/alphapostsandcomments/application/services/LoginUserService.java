package com.posada.santiago.alphapostsandcomments.application.services;

import com.posada.santiago.alphapostsandcomments.application.config.JwtTokenConfig;
import com.posada.santiago.alphapostsandcomments.application.generic.models.AuthRequest;
import com.posada.santiago.alphapostsandcomments.application.generic.models.TokenResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Slf4j
@Service
public class LoginUserService {

    private final JwtTokenConfig jwtTokenConfig;
    private final ReactiveAuthenticationManager authenticationManager;

    public Mono<TokenResponse> logIn(Mono<AuthRequest> authRequest){
        return authRequest
                .flatMap(request -> this.authenticationManager
                        .authenticate(new UsernamePasswordAuthenticationToken(
                                request.getUserEmail(),
                                request.getPassword())
                        )
                        .onErrorMap(BadCredentialsException.class,
                                err -> new BadCredentialsException("User not register please verify."))
                        .switchIfEmpty(Mono.defer(() ->
                                Mono.error(new Throwable("The login failed, please review your credentials"))))
                        .map(this.jwtTokenConfig::createToken))
                .map(jwt-> new TokenResponse(jwt))
                .doOnSuccess(user -> {
                    log.info("Login successful.");
                });
    }

}
