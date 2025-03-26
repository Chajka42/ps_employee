package ru.unisafe.psemployee.repository.r2dbc;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import ru.unisafe.psemployee.model.WebVisiting;

@Repository
public interface WebVisitingRepository extends R2dbcRepository<WebVisiting, Long> {
    Mono<WebVisiting> findWebVisitingById(Long id);
}
