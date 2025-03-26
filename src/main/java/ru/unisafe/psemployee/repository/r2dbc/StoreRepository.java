package ru.unisafe.psemployee.repository.r2dbc;

import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.unisafe.psemployee.model.Store;

@Repository
public interface StoreRepository extends R2dbcRepository<Store, Long> {
    Flux<Store> findByLogin(String login);

    @Modifying
    @Query("""
        UPDATE store
        SET def_tel_gloss = 0, def_tel_mat = 0, def_tel_spy = 0,
            def_tel_color = 0, def_lap_gloss = 0, def_lap_mat = 0, def_lap_color = 0,
            def_wat_gloss = 0, def_wat_mat = 0, def_wat_color = 0
        WHERE login = :login
    """)
    Mono<Void> resetStoreDefects(String login);
}
