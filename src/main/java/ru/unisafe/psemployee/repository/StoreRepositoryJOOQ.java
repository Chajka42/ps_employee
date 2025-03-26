package ru.unisafe.psemployee.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;

@Slf4j
@RequiredArgsConstructor
@Repository
public class StoreRepositoryJOOQ {

    private final DSLContext dsl;

    public Mono<Void> insertStore(String name, int visorId, String login) {
        return Mono.from(dsl.insertInto(table("store"))
                        .set(field("login"), login)
                        .set(field("visor_name"), name)
                        .set(field("visor_id"), visorId))
                .then();
    }
}
