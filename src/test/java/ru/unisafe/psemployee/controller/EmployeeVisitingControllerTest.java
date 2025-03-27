package ru.unisafe.psemployee.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;
import ru.unisafe.psemployee.dto.StoreItemDto;
import ru.unisafe.psemployee.dto.request.VisitStationRequest;
import ru.unisafe.psemployee.dto.response.BaseResponse;
import ru.unisafe.psemployee.service.WebVisitingService;

import java.util.Arrays;
import java.util.Objects;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebFluxTest(EmployeeVisitingController.class)
public class EmployeeVisitingControllerTest {

    @Autowired
    private WebTestClient webClient;

    @MockBean
    private WebVisitingService webVisitingService;

    private VisitStationRequest createRequest() {
        VisitStationRequest request = new VisitStationRequest();
        request.setToken("p2GjsCv0ENmVqKXpxWrk9Q0hapz3UyPV");
        request.setLogin("station2423");
        request.setCode("COD73E");
        request.setAddress("Филёвский бульвар, 6, второй этаж");
        request.setComment("");
        request.setPartnerId(5);
        request.setPartnerName("Мегафон");
        request.setProblemSolved(false);
        request.setProblemDescription("");
        request.setDefectGathering(false);
        request.setLat(55.7649836);
        request.setLon(37.4890099);
        request.setDistance(0);
        request.setProblemOrigin("-");

        StoreItemDto item1 = new StoreItemDto(2, "Телефон Глянц.(премиум)", "tel_gloss", 1);
        StoreItemDto item2 = new StoreItemDto(18, "Холдер для PS2", "knife_holder", 1);
        StoreItemDto item3 = new StoreItemDto(3, "Телефон Мат.(тонкая)", "tel_mat", 1);

        request.setDataList(Arrays.asList(item1, item2, item3));
        return request;
    }

    @Test
    public void createVisit_Success() {
        VisitStationRequest request = createRequest();

        when(webVisitingService.createVisit(any()))
                .thenReturn(Mono.just(new BaseResponse(true, "Визит успешно создан")));

        webClient.post()
                .uri("/employee/api/visits/createVisit")
                .body(BodyInserters.fromValue(request))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response -> System.out.println("Response: " + new String(Objects.requireNonNull(response.getResponseBody()))))
                .jsonPath("$.success").isEqualTo(true)
                .jsonPath("$.message").isEqualTo("Визит успешно создан");
    }

    @Test
    public void createVisit_ValidationError() {
        VisitStationRequest request = createRequest();
        request.setToken(null); // Токен обязателен

        webClient.post()
                .uri("/employee/api/visits/createVisit")
                .body(BodyInserters.fromValue(request))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .consumeWith(response -> System.out.println("Response: " + new String(Objects.requireNonNull(response.getResponseBody()))))
                .jsonPath("$.success").isEqualTo(false)
                .jsonPath("$.message").exists();
    }

    @Test
    public void createVisit_ServerError() {
        VisitStationRequest request = createRequest();

        when(webVisitingService.createVisit(any()))
                .thenReturn(Mono.error(new RuntimeException("Ошибка сервиса")));

        webClient.post()
                .uri("/employee/api/visits/createVisit")
                .body(BodyInserters.fromValue(request))
                .exchange()
                .expectStatus().is5xxServerError()
                .expectBody()
                .consumeWith(response -> System.out.println("Response: " + new String(Objects.requireNonNull(response.getResponseBody()))))
                .jsonPath("$.success").isEqualTo(false);
    }

    @Test
    public void createVisit_Duplicate() {
        VisitStationRequest request = createRequest();

        when(webVisitingService.createVisit(any()))
                .thenReturn(Mono.just(new BaseResponse(false, "Визит уже существует")));

        webClient.post()
                .uri("/employee/api/visits/createVisit")
                .body(BodyInserters.fromValue(request))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response -> System.out.println("Response: " + new String(Objects.requireNonNull(response.getResponseBody()))))
                .jsonPath("$.success").isEqualTo(false)
                .jsonPath("$.message").isEqualTo("Визит уже существует");
    }
}