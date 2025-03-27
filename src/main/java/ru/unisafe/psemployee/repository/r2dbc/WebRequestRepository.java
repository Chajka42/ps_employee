package ru.unisafe.psemployee.repository.r2dbc;

import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.unisafe.psemployee.model.WebRequest;

@Repository
public interface WebRequestRepository extends R2dbcRepository<WebRequest, Long> {

    @Query("""
        SELECT *
        FROM web_requests
        WHERE login = :login
        ORDER BY id DESC
        LIMIT 20
    """)
    Flux<WebRequest> findLatestByLogin(String login);

    @Modifying
    @Query("UPDATE web_requests SET is_received = true, was_received = NOW() WHERE id = :id LIMIT 1")
    Mono<Void> markAsReceived(@Param("id") long id);
}
