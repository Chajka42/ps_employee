package ru.unisafe.psemployee.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.unisafe.psemployee.dto.request.FirebaseTokenRequest;
import ru.unisafe.psemployee.dto.response.BaseResponse;
import ru.unisafe.psemployee.repository.FirebaseRepository;
import ru.unisafe.psemployee.service.FirebaseTokenService;

import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class FirebaseTokenServiceImpl implements FirebaseTokenService {

    private final FirebaseRepository firebaseRepository;

    @Override
    public Mono<BaseResponse> refreshFirebaseToken(FirebaseTokenRequest request) {
        log.info("FirebaseTokenRequest: {}", request);

        String token = Optional.ofNullable(request.getFirebaseToken()).orElse("none");
        log.info("FirebaseToken: {}", token);

        if (!token.equals("none")) {
            if (request.getId() != null) {
                firebaseRepository.updateFirebaseTokenById(request.getId(), token);
                log.info("Firebase token обновлен для id: {}, token: {}", request.getId(), token);
            }
        }

        return Mono.just(new BaseResponse(true, "Токен успешно обновлен"));
    }
}
