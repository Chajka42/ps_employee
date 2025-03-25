package ru.unisafe.psemployee.service;

import reactor.core.publisher.Mono;
import ru.unisafe.psemployee.dto.UpdateFieldContext;
import ru.unisafe.psemployee.dto.response.BaseResponse;

public interface DatabaseFieldUpdateService {

    Mono<Boolean> checkFieldExists(String table, String field);

    Mono<BaseResponse> updateField(UpdateFieldContext context);
}
