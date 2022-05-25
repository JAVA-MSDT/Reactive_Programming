package com.javamsdt.reactive.controller;

import com.javamsdt.reactive.modal.Contact;
import com.javamsdt.reactive.repository.ContactRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/api/contacts")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Slf4j
public class ContactController {

    private final ContactRepository contactRepository;

    @GetMapping
    public Flux<Contact> getContacts() {
        log.info("Called getContacts Controller");
        return contactRepository.findAll();
    }

    @PostMapping
    public Mono<String> addContact(@RequestBody Contact contact) {
        contactRepository.save(contact);
        return Mono.just("Added");
    }

}
