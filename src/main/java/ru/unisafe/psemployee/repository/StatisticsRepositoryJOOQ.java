package ru.unisafe.psemployee.repository;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import ru.unisafe.psemployee.dto.StatisticRecord;

import java.time.ZonedDateTime;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;

@RequiredArgsConstructor
@Repository
public class StatisticsRepositoryJOOQ {

    private final DSLContext dsl;

    public Flux<StatisticRecord> fetchStatistics(String login, String start, String end) {
        return Flux.from(dsl
                .select(
                        field("defect"),
                        field("is_learning"),
                        field("is_debugging"),
                        field("is_guarantee"),
                        field("moscow_time"),
                        field("time_zone"),
                        field("design_type"),
                        field("design_id"),
                        field("design_parent"),
                        field("design_percent"),
                        field("element_id"),
                        field("film_type"),
                        field("geo_location"),
                        field("sale")
                )
                .from(table("stats"))
                .where(field("moscow_time").between(start, end))
                .and(field("login").eq(login))
        )
        .map(this::mapToStatisticRecord);
    }

    public Flux<StatisticRecord> fetchStatistics(String login) {
        return Flux.from(dsl.select(
                                field("id"),
                                field("moscow_time"),
                                field("time_zone"),
                                field("design_type"),
                                field("design_id"),
                                field("design_percent"),
                                field("design_parent"),
                                field("defect"),
                                field("is_learning"),
                                field("is_debugging"),
                                field("is_guarantee"),
                                field("element_id"),
                                field("film_type"),
                                field("geo_location"),
                                field("sale")
                        )
                        .from(table("stats"))
                        .where(field("login").eq(login))
                        .orderBy(field("id").desc())
                        .limit(10))
                .map(this::mapToStatisticRecord);
    }

    private StatisticRecord mapToStatisticRecord(Record record) {
        return new StatisticRecord(
                record.get("defect", Boolean.class),
                record.get("is_learning", Boolean.class),
                record.get("is_debugging", Boolean.class),
                record.get("is_guarantee", Boolean.class),
                record.get("moscow_time", ZonedDateTime.class).toLocalDateTime(),
                record.get("time_zone", String.class),
                record.get("design_type", Integer.class),
                record.get("design_id", String.class),
                record.get("design_parent", String.class),
                record.get("design_percent", String.class),
                record.get("element_id", String.class),
                record.get("film_type", String.class),
                record.get("geo_location", String.class),
                record.get("sale", String.class)
        );
    }
}
