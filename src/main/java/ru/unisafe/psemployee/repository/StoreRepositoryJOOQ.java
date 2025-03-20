package ru.unisafe.psemployee.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import ru.unisafe.psemployee.dto.request.ChangeStationStoreRequest;
import ru.unisafe.psemployee.dto.response.BaseResponse;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;

@Slf4j
@RequiredArgsConstructor
@Repository
public class StoreRepositoryJOOQ {

    private final DSLContext dsl;

    public Mono<BaseResponse> changeStationStore(ChangeStationStoreRequest request) {
        Object value = parseValue(request.getType(), request.getValue());

        if (value == null) {
            return Mono.just(new BaseResponse(false, "Некорректный тип данных"));
        }

        return Mono.from(dsl.update(table("store"))
                        .set(field(request.getField()), value)
                        .where(field("name_or_key").eq(request.getLogin()))
                        .limit(1))
                .map(rowsUpdated -> rowsUpdated > 0
                        ? new BaseResponse(true, "Поле успешно обновлено")
                        : new BaseResponse(false, "Станция не найдена"))
                .doOnError(error -> log.error("Ошибка при обновлении store: {}", error.getMessage()));
    }

    private Object parseValue(String type, String value) {
        try {
            return switch (type) {
                case "bool" -> Boolean.parseBoolean(value);
                case "int" -> Integer.parseInt(value);
                default -> value;
            };
        } catch (Exception e) {
            log.error("Ошибка парсинга значения: {}", value, e);
            return null;
        }
    }
}
