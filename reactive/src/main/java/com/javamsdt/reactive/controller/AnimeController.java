package com.javamsdt.reactive.controller;

import javax.validation.Valid;

import com.javamsdt.reactive.modal.Anime;
import com.javamsdt.reactive.service.AnimeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "api/anime")
@RequiredArgsConstructor
@Slf4j
public class AnimeController {

    private final AnimeService animeService;

    @GetMapping()
    public Flux<Anime> getAllAnime() {
        log.info("Processing getAllAnime......");
        return animeService.findAllAnime();
    }

    @GetMapping(path = "{id}")
    public Mono<Anime> getAnimeById(@PathVariable int id) {
        log.info("Processing getAnimeById......");
        return animeService.findAnimeById(id);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Anime> saveAnime(@Valid @RequestBody Anime anime) {
        return animeService.saveAnime(anime);
    }

    @PutMapping(path = "{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> updateAnime(@PathVariable int id, @Valid @RequestBody Anime anime) {
        return animeService.updateAnime(anime.withId(id));
    }

    @DeleteMapping(path = "{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteAnime(@PathVariable int id) {
        return animeService.deleteAnime(id);
    }
}
