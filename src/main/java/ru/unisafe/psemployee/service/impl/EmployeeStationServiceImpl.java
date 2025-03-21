package ru.unisafe.psemployee.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.unisafe.psemployee.dto.request.ChangeCouponsRequest;
import ru.unisafe.psemployee.dto.request.ChangeStationStoreRequest;
import ru.unisafe.psemployee.dto.request.CreateStationRequest;
import ru.unisafe.psemployee.dto.request.RequestWithStationLogin;
import ru.unisafe.psemployee.dto.response.BaseResponse;
import ru.unisafe.psemployee.dto.response.CouponsInfoResponse;
import ru.unisafe.psemployee.model.Tts;
import ru.unisafe.psemployee.repository.AchievementsRepositoryJOOQ;
import ru.unisafe.psemployee.repository.EmployeeRepositoryJOOQ;
import ru.unisafe.psemployee.repository.StoreRepositoryJOOQ;
import ru.unisafe.psemployee.repository.r2dbc.TtsRepository;
import ru.unisafe.psemployee.service.EmployeeStationService;
import ru.unisafe.psemployee.service.StationUtilsService;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Slf4j
@Service
public class EmployeeStationServiceImpl implements EmployeeStationService {

    private final AchievementsRepositoryJOOQ achievementsRepository;
    private final StoreRepositoryJOOQ storeRepository;
    private final TtsRepository ttsRepository;
    private final EmployeeRepositoryJOOQ employeeRepository;
    private final StationUtilsService stationUtilsService;

    @Override
    public Mono<CouponsInfoResponse> getCouponsInfo(RequestWithStationLogin request) {
        var login = request.getLogin();

        return achievementsRepository.fetchCouponsInfo(login)
                .flatMap(couponsInfo ->
                        achievementsRepository.fetchCouponChangeList(login)
                                .collectList()
                                .map(changes -> {
                                    couponsInfo.setData(changes);
                                    return couponsInfo;
                                }))
                .doOnError(error -> log.error("Ошибка при получении информации о купонах для станции {}: {}", login, error.getMessage()));
    }

    @Override
    public Mono<BaseResponse> changeCoupons(ChangeCouponsRequest request) {
        return achievementsRepository.changeCoupons(request);
    }

    @Override
    public Mono<BaseResponse> changeStationStore(ChangeStationStoreRequest request) {
        return storeRepository.changeStationStore(request);
    }

    @Override
    public Mono<BaseResponse> createStation(CreateStationRequest request) {
        var token = request.getToken();
        log.info("Начало создания станции. Токен пользователя: {}", token);

        return employeeRepository.getEmployeeName(token)
                .zipWith(employeeRepository.getEmployeeVisorId(token))
                .doOnNext(tuple -> log.info("Получены данные сотрудника: имя = {}, visorId = {}", tuple.getT1(), tuple.getT2()))
                .flatMap(tuple -> {
                    String employeeName = tuple.getT1();
                    int visorId = tuple.getT2();

                    return ttsRepository.getLastID()
                            .defaultIfEmpty(0)
                            .doOnNext(lastID -> log.info("Последний ID станции: {}", lastID))
                            .flatMap(lastID -> {
                                String stationKey = stationUtilsService.generateRandomKey();
                                String randomToken = stationUtilsService.generateRandomToken();
                                String login = "station" + (lastID + 200);

                                log.info("Сгенерирован stationKey: {}, token: {}, login: {}", stationKey, randomToken, login);

                                var tts = Tts.builder()
                                        .stationKey(stationKey)
                                        .token(randomToken)
                                        .stationCode(request.getStationCode())
                                        .login(login)
                                        .pass("Неизвестно")
                                        .partnerId(request.getPartnerId())
                                        .plotterId(1)
                                        .visorId(visorId)
                                        .regionUnisafeId(request.getUnisafeRegion())
                                        .regionPartnerId(1)
                                        .address(request.getAddress())
                                        .ioInfo(request.getIoInfo())
                                        .comment(request.getComment())
                                        .status(true)
                                        .nLearning(0)
                                        .nDebugging(0)
                                        .timeOpen(LocalDateTime.now())
                                        .build();

                                return ttsRepository.save(tts)
                                        .doOnNext(savedTts -> log.info("Сохранена станция с login: {}, id: {}", savedTts.getLogin(), savedTts.getId()))
                                        .flatMap(savedTts -> storeRepository.insertStore(employeeName, visorId, savedTts.getLogin())
                                                .doOnSuccess(unused -> log.info("Сохранение в storeRepository успешно для login: {}", savedTts.getLogin()))
                                                .thenReturn(new BaseResponse(true, "Станция успешно создана")));
                            });
                })
                .doOnError(error -> log.error("Ошибка при создании станции: {}", error.getMessage(), error));
    }

}
