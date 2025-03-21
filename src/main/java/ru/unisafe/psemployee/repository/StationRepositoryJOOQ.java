package ru.unisafe.psemployee.repository;

import lombok.RequiredArgsConstructor;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.DatePart;
import org.jooq.Record;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.unisafe.psemployee.dto.StationFilterDto;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;

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

    public Flux<Record> findStations(StationFilterDto filter) {
        Condition condition = DSL.noCondition();

        if (filter.code() != null && !filter.code().isEmpty()) {
            condition = condition.and(field("station_code").likeIgnoreCase("%" + filter.code() + "%"));
        }
        if (filter.address() != null && !filter.address().isEmpty()) {
            condition = condition.and(field("address").likeIgnoreCase("%" + filter.address() + "%"));
        }
        if (filter.partnerId() != null) {
            condition = condition.and(field("partner_id").eq(filter.partnerId()));
        }
        if (filter.visorId() != null) {
            condition = condition.and(field("visor_id").eq(filter.visorId()));
        }
        if (filter.isActive() != null) {
            condition = condition.and(field("status").eq(filter.isActive() ? 1 : 0));
        }
        if (filter.isProblem() != null && !filter.isProblem().equals("any")) {
            condition = condition.and(field("is_problem").eq(true))
                    .and(field("problem_solved_date").le(DSL.timestampAdd(DSL.currentTimestamp(), -7, DatePart.DAY)));
        }

        return Flux.from(dslContext
                .select(
                        field("login"),
                        field("station_code"),
                        field("status"),
                        field("address"),
                        field("is_problem"),
                        field("problem_description"),
                        field("problem_solved_date"),
                        field("partner_name")
                )
                .from(table("tts"))
                .join(table("partners")).on(field("partner_id").eq(field("partners.id")))
                .where(condition)
                .orderBy(field("tts.id").desc())
                .limit(filter.size())
                .offset(filter.page() * filter.size())
        );
    }

}
