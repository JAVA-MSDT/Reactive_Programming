package com.javamsdt.reactive.modal;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.With;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@With
@AllArgsConstructor
@RequiredArgsConstructor
// @Builder
@Table(value = "anime")
public class Anime {

    @Id
    private Integer id;

    @NotNull
    @NotEmpty(message = "Name of the Anime can not be empty")
    private String name;
}
