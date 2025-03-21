package ru.unisafe.psemployee.repository.r2dbc;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import ru.unisafe.psemployee.dto.response.BaseResponse;
import ru.unisafe.psemployee.model.Tts;

import java.time.LocalDateTime;

@Repository
public interface TtsRepository extends R2dbcRepository<Tts, Integer> {

    @Query("SELECT id FROM tts ORDER BY id DESC LIMIT 1")
    Mono<Integer> getLastID();
}
