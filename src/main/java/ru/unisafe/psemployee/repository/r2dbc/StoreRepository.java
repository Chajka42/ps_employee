package ru.unisafe.psemployee.repository.r2dbc;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import ru.unisafe.psemployee.model.Store;

@Repository
public interface StoreRepository extends R2dbcRepository<Store, Long> {
    Flux<Store> findByLogin(String login);
}
