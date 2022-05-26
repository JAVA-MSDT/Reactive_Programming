package com.javamsdt.reactive.util;

import com.javamsdt.reactive.modal.Anime;

public class AnimeBuilder {

    public static Anime saveAnime() {
        return Anime.builder()
                .name("Anime Test One")
                .build();
    }

    public static Anime getAnime() {
        return Anime.builder()
                .id(1)
                .name("Anime Test One")
                .build();
    }

    public static Anime animeToBeUpdated() {
        return Anime.builder()
                .id(1)
                .name("Updated Anime Test One")
                .build();
    }

}
