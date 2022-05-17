package com.javamsdt.reactive.repository;

import com.javamsdt.reactive.modal.Contact;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ContactRepository extends ReactiveCrudRepository<Contact, Long> {

}
