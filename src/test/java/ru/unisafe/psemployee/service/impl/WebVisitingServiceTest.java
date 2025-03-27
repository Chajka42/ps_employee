package ru.unisafe.psemployee.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.unisafe.psemployee.dto.StoreItemDto;
import ru.unisafe.psemployee.dto.VisitingDto;
import ru.unisafe.psemployee.dto.request.VisitStationRequest;
import ru.unisafe.psemployee.mapper.WebVisitingMapper;
import ru.unisafe.psemployee.model.WebVisiting;
import ru.unisafe.psemployee.repository.EmployeeRepositoryJOOQ;
import ru.unisafe.psemployee.repository.QuestRepository;
import ru.unisafe.psemployee.repository.r2dbc.*;
import ru.unisafe.psemployee.service.OpenStreetMapService;
import static org.mockito.ArgumentMatchers.any;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WebVisitingServiceTest {

    @InjectMocks
    private WebVisitingServiceImpl webVisitingService;
    @Mock
    private WebVisitingRepository webVisitingRepository;
    @Mock
    private WebItemRepository webItemRepository;
    @Mock
    private EmployeeRepositoryJOOQ employeeRepository;
    @Mock
    private OpenStreetMapService openStreetMapService;
    @Mock
    private WebVisitingMapper webVisitingMapper;

    @Test
    public void createVisit() {

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


        when(employeeRepository.getEmployeeName(anyString())).thenReturn(Mono.just("John Doe"));
        when(employeeRepository.getEmployeeVisorId(anyString())).thenReturn(Mono.just(123));
        when(openStreetMapService.getAddressFromCoordinates(anyDouble(), anyDouble()))
                .thenReturn(Mono.just("Филёвский бульвар, 6"));

        when(webVisitingRepository.save(any())).thenAnswer(invocation -> {
            WebVisiting visit = invocation.getArgument(0);
            visit.setId(1L);
            return Mono.just(visit);
        });

        when(webItemRepository.saveAll(anyList())).thenReturn(Flux.empty());

        StepVerifier.create(webVisitingService.createVisit(request))
                .expectNextMatches(response -> response.isSuccess() &&
                        "Визит успешно создан".equals(response.getMessage()))
                .verifyComplete();

        verify(employeeRepository, times(1)).getEmployeeName(anyString());
        verify(employeeRepository, times(1)).getEmployeeVisorId(anyString());
        verify(openStreetMapService, times(1)).getAddressFromCoordinates(anyDouble(), anyDouble());
        verify(webVisitingRepository, times(1)).save(any(WebVisiting.class));
        verify(webItemRepository, times(1)).saveAll(anyList());
    }

    @Test
    void createVisit_Failure_VisorIdNotFound() {
        VisitStationRequest request = new VisitStationRequest();
        request.setToken("invalid_token");

        when(employeeRepository.getEmployeeVisorId(anyString())).thenReturn(Mono.empty());

        StepVerifier.create(webVisitingService.createVisit(request))
                .expectNextMatches(response -> !response.isSuccess() && response.getMessage().startsWith("Ошибка сервера"))
                .verifyComplete();
    }

    @Test
    void createVisit_Failure_SaveVisitError() {
        VisitStationRequest request = new VisitStationRequest();
        request.setToken("valid_token");

        when(employeeRepository.getEmployeeName(anyString())).thenReturn(Mono.just("John Doe"));
        when(employeeRepository.getEmployeeVisorId(anyString())).thenReturn(Mono.just(123));
        when(openStreetMapService.getAddressFromCoordinates(anyDouble(), anyDouble())).thenReturn(Mono.just("Moscow, Red Square"));
        when(webVisitingRepository.save(any())).thenReturn(Mono.error(new RuntimeException("DB error")));

        StepVerifier.create(webVisitingService.createVisit(request))
                .expectNextMatches(response -> !response.isSuccess() && response.getMessage().contains("Ошибка сервера: DB error"))
                .verifyComplete();
    }
}
