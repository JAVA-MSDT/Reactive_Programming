package com.javamsdt.reactive.repository;

import com.javamsdt.reactive.modal.Anime;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface AnimeRepository extends ReactiveCrudRepository<Anime, Integer> {

}
