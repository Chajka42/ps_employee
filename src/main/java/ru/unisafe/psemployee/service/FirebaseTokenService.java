package ru.unisafe.psemployee.service;

import org.springframework.util.MultiValueMap;
import reactor.core.publisher.Mono;
import ru.unisafe.psemployee.dto.request.FirebaseTokenRequest;
import ru.unisafe.psemployee.dto.response.BaseResponse;

public interface FirebaseTokenService {
    Mono<BaseResponse> refreshFirebaseToken(FirebaseTokenRequest request);
}
