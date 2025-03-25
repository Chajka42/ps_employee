package ru.unisafe.psemployee.repository.r2dbc;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import ru.unisafe.psemployee.model.Journal;

@Repository
public interface JournalRepository extends R2dbcRepository<Journal, Long> {

    Flux<Journal> findByLoginOrderByIdDesc(String login);
}
