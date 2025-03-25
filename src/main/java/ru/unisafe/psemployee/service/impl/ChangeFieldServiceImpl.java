package ru.unisafe.psemployee.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.unisafe.psemployee.dto.UpdateFieldContext;
import ru.unisafe.psemployee.dto.request.ChangeFieldRequest;
import ru.unisafe.psemployee.dto.response.BaseResponse;
import ru.unisafe.psemployee.service.ChangeFieldService;
import ru.unisafe.psemployee.service.DatabaseFieldUpdateService;
import ru.unisafe.psemployee.util.ValueParser;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChangeFieldServiceImpl implements ChangeFieldService {
    private final ValueParser valueParserService;
    private final DatabaseFieldUpdateService databaseUpdateService;

    @Override
    public Mono<BaseResponse> changeField(String table, String whereField, ChangeFieldRequest request) {
        return valueParserService.parseValue(request.getType(), request.getValue())
                .flatMap(parsedValue -> {
                    UpdateFieldContext context = new UpdateFieldContext(table, whereField, request, parsedValue);
                    return processFieldUpdate(context);
                })
                .onErrorResume(error -> {
                    log.error("Ошибка при обновлении {}: {}", table, error.getMessage());
                    return Mono.just(new BaseResponse(false, "Произошла ошибка: " + error.getMessage()));
                });
    }

    private Mono<BaseResponse> processFieldUpdate(UpdateFieldContext context) {
        return databaseUpdateService.checkFieldExists(context.getTable(), context.getRequest().getField())
                .flatMap(fieldExists -> {
                    if (!fieldExists) {
                        return Mono.just(new BaseResponse(false, "Некорректное поле: " + context.getRequest().getField()));
                    }
                    return databaseUpdateService.updateField(context);
                });
    }
}
