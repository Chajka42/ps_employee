package ru.unisafe.psemployee.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.unisafe.psemployee.dto.request.FirebaseTokenRequest;
import ru.unisafe.psemployee.dto.response.BaseResponse;
import ru.unisafe.psemployee.service.FirebaseTokenService;

@RequiredArgsConstructor
@RequestMapping("/employee/api")
@RestController
public class FirebaseController {

    private final FirebaseTokenService firebaseTokenService;


    @GetMapping("/refreshFirebaseToken")
    public Mono<BaseResponse> refreshFirebaseToken(
            @RequestParam(required = false) Integer id,
            @RequestParam(required = false, name = "firebase_token") String firebaseToken) {

        FirebaseTokenRequest queryParams = new FirebaseTokenRequest();
        queryParams.setId(id);
        queryParams.setFirebaseToken(firebaseToken);

        return firebaseTokenService.refreshFirebaseToken(queryParams);
    }
}
