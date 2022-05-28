package com.javamsdt.reactive.integration;

import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import com.javamsdt.reactive.modal.Anime;
import com.javamsdt.reactive.repository.AnimeRepository;
import com.javamsdt.reactive.repository.ContactRepository;
import com.javamsdt.reactive.service.AnimeService;
import com.javamsdt.reactive.util.AnimeBuilder;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.server.ResponseStatusException;
import reactor.blockhound.BlockHound;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

@ExtendWith(SpringExtension.class)
@WebFluxTest
@Import({AnimeService.class, AnimeRepository.class })
public class AnimeControllerIT {

    @MockBean
    private AnimeRepository animeRepository;

    // Temporary here, need to be removed to Contact Controller Integration Test in the future
    // Just to run the application and to avoid exception during the Integration Test
    @MockBean
    private ContactRepository contactRepository;

    @Autowired
    private WebTestClient testClient;

    private Anime anime = AnimeBuilder.getAnime();

    @BeforeAll
    public static void blockHoundSetup() {
        BlockHound.install();
    }

    @BeforeEach
    void setUp() {
        BDDMockito.when(animeRepository.findAll())
                .thenReturn(Flux.just(anime));

        BDDMockito.when(animeRepository.findById(ArgumentMatchers.anyInt()))
                .thenReturn(Mono.just(anime));

        BDDMockito.when(animeRepository.save(ArgumentMatchers.any(Anime.class)))
                .thenReturn(Mono.just(anime));

        BDDMockito.when(animeRepository.delete(ArgumentMatchers.any(Anime.class)))
                .thenReturn(Mono.empty());
    }

    @Test
    public void blockHoundWorks() {
        try {
            FutureTask<?> task = new FutureTask<>(() -> {
                Thread.sleep(0);
                return "";
            });
            Schedulers.parallel().schedule(task);

            task.get(10, TimeUnit.SECONDS);
            Assertions.fail("should fail");
        } catch (Exception e) {
            // Assertions.assertTrue(e.getCause() instanceof BlockingOperationError);
        }
    }

    @Test
    @DisplayName("getAllAnime  Anime's")
    public void getAllAnime_ReturnFluxOfAnime_WhenSuccessful() {
        testClient
                .get()
                .uri("/api/anime")
                .exchange()
                // First Option
                .expectStatus().isOk()
                .expectBodyList(Anime.class)
                .hasSize(1)
                .contains(anime);

        // Second Option
                /*.expectStatus().is2xxSuccessful()
                .expectBody()
                .jsonPath("$.[0].id").isEqualTo(anime.getId())
                .jsonPath("$.[0].name").isEqualTo(anime.getName());*/
    }

    @Test
    @DisplayName("find By Id returns Mono Of Anime")
    public void findById_ReturnMonoAnime_WhenSuccessful() {
        testClient
                .get()
                .uri("/api/anime/{id}", 1)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .jsonPath("$.id").isEqualTo(anime.getId())
                .jsonPath("$.name").isEqualTo(anime.getName());
    }

    @Test
    @DisplayName("find By Id returns Mono Error when Anime doesn't exist")
    public void findById_ReturnMonoError_WhenFailed() {
        BDDMockito.when(animeRepository.findById(ArgumentMatchers.anyInt()))
                .thenReturn(Mono.empty());

        testClient
                .get()
                .uri("/api/anime/{id}", 1)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody()
                .jsonPath("$.status").isEqualTo(404);
    }

    @Test
    @DisplayName("Save an Anime into the Database when Successful")
    public void saveAnime_StoresAnimeInDB_WhenSuccessful() {
        Anime tobSaved = AnimeBuilder.saveAnime();
        testClient
                .post()
                .uri("/api/anime")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(tobSaved))
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").isEqualTo(1)
                .jsonPath("$.name").isEqualTo("Anime Test One");
    }

    @Test
    @DisplayName("Save an Anime Returns Error When Name Is Empty")
    public void saveAnime_ReturnsError_WhenNameIsEmpty() {
        Anime tobSaved = AnimeBuilder.saveAnime().withName("");
        testClient
                .post()
                .uri("/api/anime")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(tobSaved))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.status").isEqualTo(400);
    }

    @Test
    @DisplayName("Delete anime  By Id returns nothing")
    public void deleteAnime_ReturnNothing_WhenSuccessful() {
        testClient
                .delete()
                .uri("/api/anime/{id}", 1)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    @DisplayName("Delete anime  By Id returns Mono Error when Anime doesn't exist")
    public void deleteAnime_ReturnMonoError_WhenFailed() {
        BDDMockito.when(animeRepository.findById(ArgumentMatchers.anyInt()))
                .thenReturn(Mono.empty());

        testClient
                .delete()
                .uri("/api/anime/{id}", 2)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.status").isEqualTo(404);
    }

    @Test
    @DisplayName("Update an Anime in the Database when Successful")
    public void updateAnime_UpdatesAnimeInDB_WhenSuccessful() {

        Anime toBeUpdated = AnimeBuilder.animeToBeUpdated().withId(1);
        testClient
        .put()
                .uri("/api/anime/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(toBeUpdated))
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    @DisplayName("Update an Anime in the Database when Successful")
    public void updateAnime_ReturnMonoError_WhenFailed() {
        BDDMockito.when(animeRepository.findById(ArgumentMatchers.anyInt()))
                .thenReturn(Mono.empty());

        Anime toBeUpdated = AnimeBuilder.animeToBeUpdated();
        testClient
                .put()
                .uri("/api/anime/{id}", 2)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(toBeUpdated))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.status").isEqualTo(404);
    }
}
