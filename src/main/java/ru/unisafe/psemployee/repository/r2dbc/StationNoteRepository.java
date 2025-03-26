package ru.unisafe.psemployee.repository.r2dbc;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import ru.unisafe.psemployee.model.StationNote;

@Repository
public interface StationNoteRepository extends R2dbcRepository<StationNote, Long> {

    Flux<StationNote> findByLoginAndIsActiveTrue(String login);
}