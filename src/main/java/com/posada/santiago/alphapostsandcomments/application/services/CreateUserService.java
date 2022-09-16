package com.posada.santiago.alphapostsandcomments.application.services;

import com.posada.santiago.alphapostsandcomments.application.adapters.repository.MongoUserRepository;
import com.posada.santiago.alphapostsandcomments.application.generic.models.AppUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import java.util.ArrayList;

@Slf4j
@Service
public class CreateUserService {

    private final MongoUserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public CreateUserService(MongoUserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public Mono<AppUser> createUser(AppUser newAppUser, String role){
        return repository.createUser(newAppUser.toBuilder()
                        .password(passwordEncoder.encode(newAppUser.getPassword()))
                        .userEmail(newAppUser.getUserEmail())
                        .roles(new ArrayList<>(){{add(role);}})
                        .build())
                .switchIfEmpty(Mono.defer(() -> Mono.error(new Throwable("The user could not be created"))))
                .doOnSuccess(user -> {
                    log.info("New user created: " + user.getUserEmail() + " with role: " + role);
                });
    }


}
