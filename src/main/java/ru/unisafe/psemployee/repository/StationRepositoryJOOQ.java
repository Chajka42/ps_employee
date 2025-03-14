package ru.unisafe.psemployee.repository;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import static org.jooq.impl.DSL.field;

@RequiredArgsConstructor
@Repository
public class StationRepositoryJOOQ {

    private final DSLContext dslContext;

    public Mono<String> getStationCodeByLogin(String login) {
        return Mono.from(dslContext
                .select(field("station_code"))
                        .from("tts")
                .where(field("login").eq(login)))
                .map(record -> record.get("station_code", String.class))
                .defaultIfEmpty("");
    }

}
