package ru.unisafe.psemployee.repository.r2dbc;

import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.unisafe.psemployee.model.StationNote;

@Repository
public interface StationNoteRepository extends R2dbcRepository<StationNote, Long> {

    Flux<StationNote> findByLoginAndIsActiveTrue(String login);

    @Modifying
    @Query("""
            UPDATE station_notes SET is_active = false WHERE station_notes.id = :id;
            """)
    Mono<Long> deactivateNote(Long id);
}