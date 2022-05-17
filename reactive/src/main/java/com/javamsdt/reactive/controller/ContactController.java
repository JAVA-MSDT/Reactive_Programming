package com.javamsdt.reactive.controller;

import com.javamsdt.reactive.modal.Contact;
import com.javamsdt.reactive.repository.ContactRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/api/contacts")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ContactController {

    private final ContactRepository contactRepository;
    @GetMapping
    public Flux<Contact> getContacts() {
        return contactRepository.findAll();
    }

    @PostMapping
    public Mono<String> addContact(@RequestBody Contact contact) {
        contactRepository.save(contact);
        return Mono.just("Added");
    }

}
