package com.javamsdt.reactive.service;

import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import com.javamsdt.reactive.modal.Anime;
import com.javamsdt.reactive.repository.AnimeRepository;
import com.javamsdt.reactive.util.AnimeBuilder;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.server.ResponseStatusException;
import reactor.blockhound.BlockHound;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

@ExtendWith(SpringExtension.class)
class AnimeServiceTest {

    @InjectMocks
    private AnimeService animeService;

    @Mock
    private AnimeRepository animeRepository;

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
    @DisplayName("find All Animes")
    public void findAll_ReturnFluxOfAnime_WhenSuccessful() {
        StepVerifier.create(animeService.findAllAnime())
                .expectSubscription()
                .expectNext(anime)
                .verifyComplete();
    }

    @Test
    @DisplayName("find By Id returns Mono Of Anime")
    public void findById_ReturnMonoAnime_WhenSuccessful() {
        StepVerifier.create(animeService.findAnimeById(1))
                .expectSubscription()
                .expectNext(anime)
                .verifyComplete();
    }

    @Test
    @DisplayName("find By Id returns Mono Error when Anime doesn't exist")
    public void findById_ReturnMonoError_WhenFailed() {
        BDDMockito.when(animeRepository.findById(ArgumentMatchers.anyInt()))
                .thenReturn(Mono.empty());

        StepVerifier.create(animeService.findAnimeById(2))
                .expectSubscription()
                .expectError(ResponseStatusException.class)
                .verify();
    }

    @Test
    @DisplayName("Save an Anime into the Database when Successful")
    public void saveAnime_StoresAnimeInDB_WhenSuccessful() {
        Anime tobSaved = AnimeBuilder.saveAnime();
        StepVerifier.create(animeService.saveAnime(tobSaved))
                .expectSubscription()
                .expectNext(anime)
                .verifyComplete();
    }

    @Test
    @DisplayName("Delete anime  By Id returns nothing")
    public void deleteAnime_ReturnNothing_WhenSuccessful() {
        StepVerifier.create(animeService.deleteAnime(1))
                .expectSubscription()
                .verifyComplete();
    }

    @Test
    @DisplayName("fDelete anime  By Id returns Mono Error when Anime doesn't exist")
    public void deleteAnime_ReturnMonoError_WhenFailed() {
        BDDMockito.when(animeRepository.findById(ArgumentMatchers.anyInt()))
                .thenReturn(Mono.empty());

        StepVerifier.create(animeService.deleteAnime(2))
                .expectSubscription()
                .expectError(ResponseStatusException.class)
                .verify();
    }
}
