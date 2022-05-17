package com.javamsdt.reactive.modal;

import java.time.LocalDate;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table(value = "contact")
public class Contact {

    @Id
    @Column(value = "contact_id")
    private Long contactId;

    @Column(value = "firstname")
    private String firstName;

    @Column(value = "lastname")
    private String lastName;

    @Column(value = "email")
    private String email;

    @Column(value = "phone_number")
    private String phoneNumber;

    @Column(value = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(value = "image_url")
    private String imageURL;

}
