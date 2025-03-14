package ru.unisafe.psemployee.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import static org.jooq.impl.DSL.field;

@RequiredArgsConstructor
@Slf4j
@Repository
public class PartnerRepositoryJOOQ {

    private final DSLContext dslContext;

    public Mono<String> getPartnerName(int partnerId) {
        return Mono.from(
                dslContext.select(field("partner_name"))
                        .from("partners")
                        .where("id = ?", partnerId))
                .map(record -> record.get("partner_name", String.class));
    }

    public Mono<Integer> getPartnerIdByStation(String login) {
        return Mono.from(
                dslContext.select(field("partner_id"))
                        .from("tts")
                        .where("login = ?", login))
                .map(record -> record.get("partner_id", Integer.class));
    }

}
