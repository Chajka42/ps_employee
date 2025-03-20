package ru.unisafe.psemployee.repository;

import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;

@Slf4j
@Repository
public class FirebaseRepositoryJOOQ {

    private final DSLContext dsl;

    public FirebaseRepositoryJOOQ(DSLContext dsl) {
        this.dsl = dsl;
    }

    public void updateFirebaseTokenById(int id, String token) {
        log.info("Обновление токена для id: {}", id);

        Mono.from(dsl
                .update(table("employee"))
                .set(field("firebase"), token)
                .where(field("id").eq(id))).subscribe();
    }

    public Mono<String> findFcmTokenByStationCodeAndPartnerId(String stationCode, int partnerId) {
        return Mono.from(
                        dsl.select(field("fcm_token"))
                                .from(table("tts"))
                                .where(field("station_code").eq(stationCode))
                                .and(field("partner_id").eq(partnerId))
                )
                .map(record -> record.get("fcm_token", String.class))
                .defaultIfEmpty("?");
    }

}
