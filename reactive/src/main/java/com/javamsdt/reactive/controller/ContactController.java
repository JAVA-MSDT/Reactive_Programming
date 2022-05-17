package com.javamsdt.reactive.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/api/contacts")
@CrossOrigin(origins = "*")
public class ContactController {

    @GetMapping
    public Mono<String> getContacts() {
        return Mono.just("Contacts");
    }

}
