package ru.unisafe.psemployee.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.test.StepVerifier;
import ru.unisafe.psemployee.dto.request.FirebaseTokenRequest;
import ru.unisafe.psemployee.repository.FirebaseRepositoryJOOQ;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FirebaseTokenServiceTest {

    @InjectMocks
    private FirebaseTokenServiceImpl firebaseTokenService;

    @Mock
    private FirebaseRepositoryJOOQ firebaseRepository;

    @DisplayName("Передача валидного токена и id")
    @Test
    void refreshFirebaseToken_Success() {
        FirebaseTokenRequest request = new FirebaseTokenRequest();
        request.setId(123);
        request.setFirebaseToken("validToken");

        StepVerifier.create(firebaseTokenService.refreshFirebaseToken(request))
                .expectNextMatches(response -> response.isSuccess() && response.getMsg().equals("Токен успешно обновлен"))
                .verifyComplete();

        verify(firebaseRepository, times(1)).updateFirebaseTokenById(123, "validToken");
    }

    @DisplayName("Не передан параметр токен")
    @Test
    void refreshFirebaseToken_Failure_MissingParams() {
        FirebaseTokenRequest request = new FirebaseTokenRequest();
        request.setId(123);

        StepVerifier.create(firebaseTokenService.refreshFirebaseToken(request))
                .expectNextMatches(response -> response.isSuccess() && response.getMsg().equals("Токен успешно обновлен"))
                .verifyComplete();

        verify(firebaseRepository, never()).updateFirebaseTokenById(anyInt(), anyString());
    }

    @DisplayName("Параметр токен = none")
    @Test
    void refreshFirebaseToken_Failure_NoneToken() {
        FirebaseTokenRequest request = new FirebaseTokenRequest();
        request.setId(123);
        request.setFirebaseToken("none");

        StepVerifier.create(firebaseTokenService.refreshFirebaseToken(request))
                .expectNextMatches(response -> response.isSuccess() && response.getMsg().equals("Токен успешно обновлен"))
                .verifyComplete();

        verify(firebaseRepository, never()).updateFirebaseTokenById(anyInt(), anyString());
    }

    @DisplayName("Параметр id = null")
    @Test
    void refreshFirebaseToken_Failure_NullId() {
        FirebaseTokenRequest request = new FirebaseTokenRequest();
        request.setId(null);
        request.setFirebaseToken("validToken");

        StepVerifier.create(firebaseTokenService.refreshFirebaseToken(request))
                .expectNextMatches(response -> response.isSuccess() && response.getMsg().equals("Токен успешно обновлен"))
                .verifyComplete();

        verify(firebaseRepository, never()).updateFirebaseTokenById(anyInt(), anyString());
    }

}
