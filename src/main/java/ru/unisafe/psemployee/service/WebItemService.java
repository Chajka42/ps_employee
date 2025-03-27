package ru.unisafe.psemployee.service;

import reactor.core.publisher.Mono;
import ru.unisafe.psemployee.dto.request.AddWebItemRequest;
import ru.unisafe.psemployee.dto.response.BaseResponse;

public interface WebItemService {
    Mono<BaseResponse> addRequestItem(AddWebItemRequest request);
}
