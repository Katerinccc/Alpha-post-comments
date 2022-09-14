package com.posada.santiago.alphapostsandcomments.application.config;

import com.posada.santiago.alphapostsandcomments.application.adapters.repository.MongoUserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;

@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    SecurityWebFilterChain springSecurityAccess(ServerHttpSecurity httpSecurity,
                                                JwtTokenConfig tokenConfig,
                                                ReactiveAuthenticationManager reactiveAuthenticationManager) {

        final String CREATE_POST = "/create/post";
        final String CREATE_USERS ="/auth/create/**";

        return httpSecurity.csrf(ServerHttpSecurity.CsrfSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .authenticationManager(reactiveAuthenticationManager)
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                .authorizeExchange( access -> access
                        .pathMatchers(CREATE_POST).hasAuthority("ROLE_USER")
                        .pathMatchers(CREATE_USERS).hasAuthority("ROLE_ADMIN")
                        .anyExchange().permitAll()
                ).addFilterAt(new JwtFilter(tokenConfig), SecurityWebFiltersOrder.HTTP_BASIC)
                .build();

    }


    @Bean
    public ReactiveUserDetailsService userDetailsService(MongoUserRepository repository) {
        return username -> repository.findUserByEmail(username)
                .map(user -> User
                        .withUsername(user.getUserEmail()).password(user.getPassword())
                        .authorities(user.getRoles().toArray(new String[0]))
                        .accountExpired(!user.isActive())
                        .credentialsExpired(!user.isActive())
                        .disabled(!user.isActive())
                        .accountLocked(!user.isActive())
                        .build()
                );
    }

    @Bean
    public ReactiveAuthenticationManager reactiveAuthenticationManager(ReactiveUserDetailsService userDetailsService,
                                                                       PasswordEncoder passwordEncoder){
        var authenticationManager = new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService);
        authenticationManager.setPasswordEncoder(passwordEncoder);
        return authenticationManager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }


}