package ru.unisafe.psemployee.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.unisafe.psemployee.dto.request.CreateStationRequest;
import ru.unisafe.psemployee.dto.response.BaseResponse;
import ru.unisafe.psemployee.model.Tts;
import ru.unisafe.psemployee.repository.EmployeeRepositoryJOOQ;
import ru.unisafe.psemployee.repository.StoreRepositoryJOOQ;
import ru.unisafe.psemployee.repository.r2dbc.TtsRepository;
import ru.unisafe.psemployee.service.StationUtilsService;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeStationServiceImplTest {

    @Mock
    private StoreRepositoryJOOQ storeRepository;
    @Mock
    private TtsRepository ttsRepository;
    @Mock
    private EmployeeRepositoryJOOQ employeeRepository;
    @Mock
    private StationUtilsService stationUtilsService;

    @InjectMocks
    private EmployeeStationServiceImpl employeeStationService;

    @Test
    void createStation_ShouldCreateSuccessfully() {

        CreateStationRequest request = new CreateStationRequest();
        request.setToken("userToken");
        request.setStationCode("ST123");
        request.setPartnerId(100);
        request.setUnisafeRegion(5);
        request.setAddress("Some Address");
        request.setIoInfo("IO Info");
        request.setComment("Test Comment");

        when(employeeRepository.getEmployeeName("userToken")).thenReturn(Mono.just("John Doe"));
        when(employeeRepository.getEmployeeVisorId("userToken")).thenReturn(Mono.just(123));
        when(ttsRepository.getLastID()).thenReturn(Mono.just(10));
        when(stationUtilsService.generateRandomKey()).thenReturn("randomKey");
        when(stationUtilsService.generateRandomToken()).thenReturn("randomToken");
        when(ttsRepository.save(any(Tts.class))).thenAnswer(invocation -> {
            Tts savedTts = invocation.getArgument(0);
            savedTts.setId(1);
            return Mono.just(savedTts);
        });
        when(storeRepository.insertStore("John Doe", 123, "station210"))
                .thenReturn(Mono.empty());


        Mono<BaseResponse> responseMono = employeeStationService.createStation(request);

        StepVerifier.create(responseMono)
                .expectNextMatches(response -> response.isSuccess() && "Станция успешно создана".equals(response.getMsg()))
                .verifyComplete();
    }

    @Test
    void createStation_ShouldHandleError() {
        CreateStationRequest request = new CreateStationRequest();
        request.setToken("userToken");

        when(employeeRepository.getEmployeeName("userToken"))
                .thenReturn(Mono.error(new RuntimeException("Database error")));
        when(employeeRepository.getEmployeeVisorId("userToken"))
                .thenReturn(Mono.just(123));

        Mono<BaseResponse> responseMono = employeeStationService.createStation(request);

        StepVerifier.create(responseMono)
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("Database error"))
                .verify();
    }
}
