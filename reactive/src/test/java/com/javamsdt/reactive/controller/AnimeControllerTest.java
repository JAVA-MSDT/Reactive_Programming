package com.javamsdt.reactive.controller;

import java.util.List;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import com.javamsdt.reactive.modal.Anime;
import com.javamsdt.reactive.repository.AnimeRepository;
import com.javamsdt.reactive.service.AnimeService;
import com.javamsdt.reactive.util.AnimeBuilder;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.server.ResponseStatusException;
import reactor.blockhound.BlockHound;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class AnimeControllerTest {

    @InjectMocks
    private AnimeController animeController;

    @Mock
    private AnimeService animeService;

    private Anime anime = AnimeBuilder.getAnime();
    private Anime tobSaved = AnimeBuilder.saveAnime();

    @BeforeAll
    public static void blockHoundSetup() {
        BlockHound.install();
    }

    @BeforeEach
    void setUp() {
        BDDMockito.when(animeService.findAllAnime())
                .thenReturn(Flux.just(anime));

       BDDMockito.when(animeService.findAnimeById(ArgumentMatchers.anyInt()))
                .thenReturn(Mono.just(anime));

         BDDMockito.when(animeService.saveAnime(ArgumentMatchers.any(Anime.class)))
                .thenReturn(Mono.just(anime));

        BDDMockito.when(animeService.batchSaveAnime(List.of(tobSaved,tobSaved)))
                .thenReturn(Flux.just(tobSaved, tobSaved));

        BDDMockito.when(animeService.deleteAnime(ArgumentMatchers.anyInt()))
                .thenReturn(Mono.empty());

        BDDMockito.when(animeService.updateAnime(ArgumentMatchers.any(Anime.class)))
                .thenReturn(Mono.empty());
    }

    @AfterEach
    void tearDown() {
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
        StepVerifier.create(animeController.getAllAnime())
                .expectSubscription()
                .expectNext(anime)
                .verifyComplete();
    }

    @Test
    @DisplayName("find By Id returns Mono Of Anime")
    public void findById_ReturnMonoAnime_WhenSuccessful() {
        StepVerifier.create(animeController.getAnimeById(1))
                .expectSubscription()
                .expectNext(anime)
                .verifyComplete();
    }

    @Test
    @DisplayName("Save an Anime into the Database when Successful")
    public void saveAnime_StoresAnimeInDB_WhenSuccessful() {
        StepVerifier.create(animeController.saveAnime(tobSaved))
                .expectSubscription()
                .expectNext(anime)
                .verifyComplete();
    }

    @Test
    @DisplayName("batchSaveAnime into the Database when Successful")
    public void batchSaveAnime_StoresListOfAnimeInDB_WhenSuccessful() {
        StepVerifier.create(animeService.batchSaveAnime(List.of(tobSaved, tobSaved)))
                .expectSubscription()
                .expectNext(tobSaved, tobSaved)
                .verifyComplete();
    }

    @Test
    @DisplayName("Delete anime  By Id returns nothing")
    public void deleteAnime_ReturnNothing_WhenSuccessful() {
        StepVerifier.create(animeController.deleteAnime(1))
                .expectSubscription()
                .verifyComplete();
    }

    @Test
    @DisplayName("Update an Anime in the Database when Successful")
    public void updateAnime_UpdatesAnimeInDB_WhenSuccessful() {

        Anime toBeUpdated = AnimeBuilder.animeToBeUpdated();
        StepVerifier.create(animeController.updateAnime(toBeUpdated.getId(), toBeUpdated))
                .expectSubscription()
                .verifyComplete();
    }

}
