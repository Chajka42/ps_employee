package ru.unisafe.psemployee.repository;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import ru.unisafe.psemployee.dto.StatisticRecord;

import java.time.LocalDateTime;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;

@RequiredArgsConstructor
@Repository
public class StatisticsRepositoryJOOQ {

    private final DSLContext dsl;

    public Flux<StatisticRecord> fetchStatistics(String login, String start, String end) {
        return Flux.from(dsl
                .select(
                        field("defect", Boolean.class),
                        field("is_learning", Boolean.class),
                        field("is_debugging", Boolean.class),
                        field("is_guarantee", Boolean.class),
                        field("moscow_time", LocalDateTime.class),
                        field("time_zone", String.class),
                        field("design_type", Integer.class),
                        field("design_id", String.class),
                        field("design_parent", String.class),
                        field("design_percent", String.class),
                        field("element_id", String.class),
                        field("film_type", String.class),
                        field("geo_location", String.class),
                        field("sale", String.class)
                )
                .from(table("stats"))
                .where(field("moscow_time").between(start, end))
                .and(field("login").eq(login))
        )
        .map(this::mapToStatisticRecord);
    }

    private StatisticRecord mapToStatisticRecord(Record record) {
        return new StatisticRecord(
                record.get("defect", Boolean.class),
                record.get("is_learning", Boolean.class),
                record.get("is_debugging", Boolean.class),
                record.get("is_guarantee", Boolean.class),
                record.get("moscow_time", LocalDateTime.class),
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
