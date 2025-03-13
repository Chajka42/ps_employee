package ru.unisafe.psemployee.repository;

import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;

@Slf4j
@Repository
public class FirebaseRepository {

    private final DSLContext dsl;

    public FirebaseRepository(DSLContext dsl) {
        this.dsl = dsl;
    }

    public void updateFirebaseTokenById(int id, String token) {
        log.info("Обновление токена для id: {}", id);

        Mono.from(dsl
                .update(table("employee"))
                .set(field("firebase"), token)
                .where(field("id").eq(id))).subscribe();
    }

}
