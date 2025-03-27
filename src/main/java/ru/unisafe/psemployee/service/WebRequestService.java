package ru.unisafe.psemployee.service;

import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.unisafe.psemployee.dto.request.WebRequestReceiveRequest;
import ru.unisafe.psemployee.dto.response.BaseResponse;
import ru.unisafe.psemployee.model.WebRequest;

public interface WebRequestService {
    Flux<WebRequest> getReceivingList(String searchParam);

    Mono<ResponseEntity<BaseResponse>> receiveRequest(WebRequestReceiveRequest request);

    Mono<ResponseEntity<BaseResponse>> deleteRequest(WebRequestReceiveRequest request);
}
