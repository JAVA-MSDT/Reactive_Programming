package com.javamsdt.reactive.config;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

@EnableWebFluxSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity serverHttpSecurity) {
        return
                serverHttpSecurity
                        .csrf().disable()
                        .authorizeExchange()
                        .pathMatchers(HttpMethod.POST, "/api/anime/**").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.GET).hasAnyRole("USER")
                        .anyExchange().authenticated()
                        .and()
                        .formLogin()
                        .and()
                        .httpBasic()
                        .and()
                        .build();
    }

    // In Memory Security using Form Login Basic Auth
    @Bean
    public MapReactiveUserDetailsService userDetailsService() {
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        UserDetails admin = User.withUsername("admin")
                .password(encoder.encode("admin"))
                .roles("ADMIN")
                .build();

        UserDetails user = User.withUsername("user")
                .password(encoder.encode("user"))
                .roles("USER")
                .build();

        return new MapReactiveUserDetailsService(admin, user);
    }
}
