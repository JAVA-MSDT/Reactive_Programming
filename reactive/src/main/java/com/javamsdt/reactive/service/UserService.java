package com.javamsdt.reactive.service;

import com.javamsdt.reactive.modal.User;
import com.javamsdt.reactive.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
public class UserService implements ReactiveUserDetailsService {

    private final UserRepository userRepository;

    public Mono<UserDetails> findByUsername(String username) {
        userRepository.findByUsername(username).doOnNext(user -> System.out.println("findByUsername:: " + user.getPassword()));
        return userRepository.findByUsername(username)
                .cast(UserDetails.class);
    }

    public Mono<User> saveUser(User user) {
        System.out.println("For Testing only User Password Before Encryption:: " +  user.getPassword());
        user.setPassword(PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(user.getPassword()));
        System.out.println("For Testing only User Password After Encryption:: " +  user.getPassword());
        return userRepository.save(user);
    }

    public Flux<User> findAllUsers() {
        return userRepository.findAll();
    }
}
