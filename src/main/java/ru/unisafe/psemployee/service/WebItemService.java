package ru.unisafe.psemployee.service;

import reactor.core.publisher.Mono;
import ru.unisafe.psemployee.dto.request.AddWebItemRequest;
import ru.unisafe.psemployee.dto.request.GetRequestInfoRequest;
import ru.unisafe.psemployee.dto.response.BaseResponse;
import ru.unisafe.psemployee.dto.response.RequestInfoResponse;

public interface WebItemService {
    Mono<BaseResponse> addRequestItem(AddWebItemRequest request);

    Mono<RequestInfoResponse> getRequestInfo(GetRequestInfoRequest request);

}
