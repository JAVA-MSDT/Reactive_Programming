package com.javamsdt.reactive.service;

import com.javamsdt.reactive.modal.Anime;
import com.javamsdt.reactive.repository.AnimeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnimeService {

    private final AnimeRepository animeRepository;

    public Flux<Anime> findAllAnime() {
        return animeRepository.findAll();
    }

    public Mono<Anime> findAnimeById(int id) {
        return animeRepository.findById(id)
                .switchIfEmpty(monoResponseStatusNotFoundException());
    }

    public <T> Mono<T> monoResponseStatusNotFoundException() {
        return Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Anime Not Found"));
    }

    public Mono<Anime> saveAnime(Anime anime) {
        return animeRepository.save(anime);
    }

    public Mono<Void> updateAnime(Anime anime) {
        log.info(anime.toString());
        return findAnimeById(anime.getId())
                .map(animeFounded -> anime.withId(animeFounded.getId()))
                .flatMap(animeRepository::save)
                .thenEmpty(Mono.empty());
    }

    public Mono<Void> deleteAnime(int id) {
        return findAnimeById(id).flatMap(animeRepository::delete);
    }
}
