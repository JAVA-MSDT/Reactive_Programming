package com.javamsdt.reactive.config;

import com.javamsdt.reactive.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity serverHttpSecurity) {
        return
                serverHttpSecurity
                        .csrf().disable()
                        .authorizeExchange()
                        .pathMatchers(HttpMethod.POST, "/api/anime/**").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.GET, "/api/anime/**").hasRole("USER")
                        .pathMatchers(HttpMethod.POST, "/api/users/**").permitAll()
                        .anyExchange().authenticated()
                        .and()
                        .formLogin()
                        .and()
                        .httpBasic()
                        .and()
                        .build();
    }

    @Bean
    ReactiveAuthenticationManager reactiveAuthenticationManager(UserService userService) {
        return new UserDetailsRepositoryReactiveAuthenticationManager(userService);
    }
}
