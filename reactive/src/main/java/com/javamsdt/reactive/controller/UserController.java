package com.javamsdt.reactive.controller;

import com.javamsdt.reactive.modal.User;
import com.javamsdt.reactive.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/api/users")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Flux<User> getAllUsers() {
        return userService.findAllUsers();
    }

    @PostMapping
    public Mono<User> saveUser(@RequestBody User user) {
        return userService.saveUser(user);
    }
}
