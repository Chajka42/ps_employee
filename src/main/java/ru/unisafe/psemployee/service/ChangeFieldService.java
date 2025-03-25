package ru.unisafe.psemployee.service;

import reactor.core.publisher.Mono;
import ru.unisafe.psemployee.dto.request.ChangeFieldRequest;
import ru.unisafe.psemployee.dto.response.BaseResponse;

public interface ChangeFieldService {
    Mono<BaseResponse> changeField(String table, String whereField, ChangeFieldRequest request);
}
