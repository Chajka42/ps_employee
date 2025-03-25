package ru.unisafe.psemployee.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import ru.unisafe.psemployee.dto.UpdateFieldContext;
import ru.unisafe.psemployee.dto.response.BaseResponse;
import ru.unisafe.psemployee.service.DatabaseFieldUpdateService;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;

@Slf4j
@RequiredArgsConstructor
@Component
public class DatabaseFieldUpdateServiceImpl implements DatabaseFieldUpdateService {

    private final DSLContext dslContext;

    @Override
    public Mono<Boolean> checkFieldExists(String table, String field) {
        return Mono.from(
                dslContext.select(field(field))
                        .from(table(table))
                        .limit(1)
        ).hasElement();
    }

    @Override
    public Mono<BaseResponse> updateField(UpdateFieldContext context) {
        String table = context.getTable();
        String field = context.getRequest().getField();
        String whereField = context.getWhereField();
        String login = context.getRequest().getLogin();
        Object value = context.getParsedValue();

        log.info("Обновление {}.{}. Поле в where clause: {}. Для {}: {} (тип: {})",
                table, field, whereField, login, value, value != null ? value.getClass().getSimpleName() : "null");

        if (!(value instanceof Integer || value instanceof String || value instanceof Boolean)) {
            return Mono.just(new BaseResponse(false, "Некорректный тип данных для обновления"));
        }

        Object finalValue = value instanceof Boolean boolValue ? (boolValue ? 1 : 0) : value;

        return Mono.from(
                dslContext.select(field(field))
                        .from(table(table))
                        .where(field(whereField).eq(login))
        ).flatMap(currentValue -> {
            if (currentValue.getValue(0).equals(finalValue)) {
                return Mono.just(new BaseResponse(true, "Значение уже установлено"));
            }
            return Mono.from(dslContext.update(table(table))
                            .set(field(field), finalValue)
                            .where(field(whereField).eq(login))
                            .limit(1))
                    .map(rowsUpdated -> rowsUpdated > 0
                            ? new BaseResponse(true, "Поле успешно обновлено")
                            : new BaseResponse(false, "Запись не найдена"));
        });
    }
}
