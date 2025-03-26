package ru.unisafe.psemployee.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.unisafe.psemployee.dto.StationFilterDto;
import ru.unisafe.psemployee.dto.StationRecord;
import ru.unisafe.psemployee.dto.request.*;
import ru.unisafe.psemployee.dto.response.BaseResponse;
import ru.unisafe.psemployee.dto.response.CouponsInfoResponse;
import ru.unisafe.psemployee.dto.response.MasterKeyResponse;
import ru.unisafe.psemployee.dto.response.StationInfoResponse;
import ru.unisafe.psemployee.model.*;
import ru.unisafe.psemployee.repository.AchievementsRepositoryJOOQ;
import ru.unisafe.psemployee.repository.EmployeeRepositoryJOOQ;
import ru.unisafe.psemployee.repository.StationRepositoryJOOQ;
import ru.unisafe.psemployee.repository.StoreRepositoryJOOQ;
import ru.unisafe.psemployee.repository.r2dbc.JournalRepository;
import ru.unisafe.psemployee.repository.r2dbc.StoreItemsRepository;
import ru.unisafe.psemployee.repository.r2dbc.TtsRepository;
import ru.unisafe.psemployee.service.ChangeFieldService;
import ru.unisafe.psemployee.service.EmployeeStationService;
import ru.unisafe.psemployee.service.StationUtilsService;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class EmployeeStationServiceImpl implements EmployeeStationService {

    private final AchievementsRepositoryJOOQ achievementsRepository;
    private final EmployeeRepositoryJOOQ employeeRepository;
    private final JournalRepository journalRepository;
    private final StationRepositoryJOOQ stationRepositoryJOOQ;
    private final StoreRepositoryJOOQ storeRepository;
    private final StoreItemsRepository storeItemsRepository;
    private final TtsRepository ttsRepository;

    private final ChangeFieldService changeFieldService;
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
    public Mono<BaseResponse> changeStationStore(ChangeFieldRequest request) {
        return changeFieldService.changeField("store", "login", request);
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

    @Override
    public Flux<StationRecord> findStations(StationFilterDto filter) {
        return stationRepositoryJOOQ.findStations(filter)
                .map(record -> new StationRecord(
                        record.get("login", String.class),
                        record.get("station_code", String.class),
                        record.get("status", Integer.class),
                        record.get("address", String.class),
                        record.get("is_problem", Boolean.class),
                        record.get("problem_description", String.class),
                        record.get("problem_solved_date", ZonedDateTime.class).toLocalDateTime(),
                        record.get("partner_name", String.class)
                ));
    }

    @Override
    public Mono<StationInfoResponse> getStationInfo(String login) {
        return ttsRepository.getStationInfo(login)
                .flatMap(stationInfo -> {
                    StationInfoResponse stationInfoResponse = new StationInfoResponse();
                    stationInfoResponse.setStationInfo(stationInfo);
                    stationInfoResponse.setSuccess(Boolean.TRUE);
                    stationInfoResponse.setMessage("Станция успешно найдена");
                    return Mono.just(stationInfoResponse);
                })
                .switchIfEmpty(Mono.just(new StationInfoResponse("Станция не найдена", false, null)))
                .onErrorResume(error -> {
                    log.error("Произошла ошибка при поиске станции: {}", error.getMessage(), error);
                    return Mono.just(new StationInfoResponse("Произошла ошибка", false, null));
                });
    }

    @Override
    public Mono<StationInfoResponse> getJournalInfo(String login) {
        return ttsRepository.getStationInfo(login)
                .flatMap(stationInfo ->
                        journalRepository.findByLoginOrderByIdDesc(login)
                                .collectList()
                                .map(journalInfo -> {
                                    stationInfo.setData(journalInfo);

                                    StationInfoResponse response = new StationInfoResponse();
                                    response.setStationInfo(stationInfo);
                                    response.setSuccess(true);
                                    response.setMessage("Станция успешно найдена");

                                    return response;
                                })
                )
                .switchIfEmpty(Mono.just(new StationInfoResponse("Станция не найдена", false, null)))
                .onErrorResume(error -> {
                    log.error("Ошибка при получении данных: {}", error.getMessage(), error);
                    return Mono.just(new StationInfoResponse("Произошла ошибка", false, null));
                });
    }

    @Override
    public Mono<StationInfoResponse> getStationMenuInfo(String login) {
        return ttsRepository.getStationMenuInfo(login)
                .flatMap(stationInfo -> {
                    StationInfoResponse stationInfoResponse = new StationInfoResponse();
                    stationInfoResponse.setStationInfo(stationInfo);
                    stationInfoResponse.setSuccess(Boolean.TRUE);
                    stationInfoResponse.setMessage("Станция успешно найдена");
                    return Mono.just(stationInfoResponse);
                })
                .switchIfEmpty(Mono.just(new StationInfoResponse("Станция не найдена", false, null)))
                .onErrorResume(error -> {
                    log.error("Произошла ошибка при поиске станции: {}", error.getMessage(), error);
                    return Mono.just(new StationInfoResponse("Произошла ошибка", false, null));
                });
    }

    @Override
    public Mono<StationInfoResponse> getStationInfoSupport(String login) {
        return ttsRepository.getStationInfoSupport(login)
                .flatMap(stationInfo -> {
                    StationInfoResponse stationInfoResponse = new StationInfoResponse();
                    stationInfoResponse.setStationInfo(stationInfo);
                    stationInfoResponse.setSuccess(Boolean.TRUE);
                    stationInfoResponse.setMessage("Станция успешно найдена");
                    return Mono.just(stationInfoResponse);
                })
                .switchIfEmpty(Mono.just(new StationInfoResponse("Станция не найдена", false, null)))
                .onErrorResume(error -> {
                    log.error("Произошла ошибка при поиске станции: {}", error.getMessage(), error);
                    return Mono.just(new StationInfoResponse("Произошла ошибка", false, null));
                });
    }

    @Override
    public Mono<StationInfoResponse> getStationInfoWithItemList(String login) {
        return ttsRepository.getStationInfo(login)
                .flatMap(stationInfo ->
                        storeItemsRepository.findByPartnerId(Integer.parseInt(stationInfo.getPartnerId()))
                                .collectList()
                                .map(itemList -> {
                                    stationInfo.setData(itemList);

                                    return StationInfoResponse.builder()
                                            .stationInfo(stationInfo)
                                            .success(true)
                                            .message("Станция успешно найдена")
                                            .build();
                                })
                )
                .switchIfEmpty(Mono.just(new StationInfoResponse("Станция не найдена", false, null)))
                .onErrorResume(error -> {
                    log.error("Ошибка при получении данных: {}", error.getMessage(), error);
                    return Mono.just(new StationInfoResponse("Произошла ошибка", false, null));
                });
    }

    @Override
    public Mono<BaseResponse> updateStationField(ChangeFieldRequest request) {
        return changeFieldService.changeField("tts", "login", request);
    }

    @Override
    public Mono<MasterKeyResponse> getMasterKey(MasterKeyRequest request) {
        return employeeRepository.getEmployeeVisorId(request.getToken())
                .flatMap(foundedVisorId -> ttsRepository.getStationKeyByLogin(request.getLogin())
                        .flatMap(tts -> {
                            String newKey = stationUtilsService.generateMasterKeyNewOne(tts.getStationKey(), foundedVisorId);
                            return Mono.just(new MasterKeyResponse(tts.getStationKey(), newKey, true, "Мастер ключ обновлен успешно"));
                        })
                        .switchIfEmpty(Mono.just(new MasterKeyResponse(null, null, false, "Станция не найдена")))
                )
                .switchIfEmpty(Mono.just(new MasterKeyResponse(null, null, false, "Сотрудник не найден или токен неверен")))
                .onErrorResume(e -> {
                    log.error("Ошибка при получении мастер-ключа: {}", e.getMessage(), e);
                    return Mono.just(new MasterKeyResponse(null, null, false, "Внутренняя ошибка сервера"));
                });
    }

    @Override
    public Mono<BaseResponse> saveSupportRequest(AddJournalRequest request) {
        if (request.getProblemText().length() < 13) {
            return Mono.just(new BaseResponse(false, "Описание слишком короткое"));
        } else if (request.getProblemText().length() > 250) {
            return Mono.just(new BaseResponse(false, "Описание слишком длинное"));
        }

        return employeeRepository.getEmployeeName(request.getToken())
                .flatMap(techSupportName -> {
                    Journal journal = new Journal();
                    journal.setTechPodId(0L);
                    journal.setLogin(request.getLogin());
                    journal.setTechPodName(techSupportName);
                    journal.setPartnerId(request.getPartnerId());
                    journal.setPartnerCode(request.getCode());
                    journal.setManagerId(request.getVisorId());
                    journal.setManagerName(request.getVisorName());
                    journal.setProblemType(request.getProblemType());
                    journal.setProblemText(request.getProblemText());
                    journal.setIsYes(!request.getResolved());
                    journal.setIsManagerRequired(false);
                    journal.setDate(ZonedDateTime.now());

                    return journalRepository.save(journal)
                            .then(sendNotificationIfNeeded(request))
                            .thenReturn(new BaseResponse(true, "Запрос успешно сохранен"));
                })
                .onErrorResume(e -> Mono.just(new BaseResponse(false, "Внутренняя ошибка сервера: " + e.getMessage())));
    }

    private Mono<Void> sendNotificationIfNeeded(AddJournalRequest request) {
        if (!request.getResolved()) {
            return Mono.empty();
        }

        String message = "Тех.Под.Алерт: станция " + request.getLogin() + " требует твоего срочного вмешательства! Почитай журнал станции.";

        //TODO Дописать этот ужас. Заставить FCM работать
//        return getVisorIdByStation(request.getLogin())
//                .flatMap(visorId -> getEmployeeIdByVisorId(visorId)
//                        .flatMap(employeeId -> employeeConsole.getEmployeeFirebaseToken(employeeId)
//                                .flatMap(token -> cfmService.sendNotification("title", message, token)
//                                        .then(cfmEmployee.insertNewNotification(message, request.getLogin(), employeeId))
//                                )
//                        )
//                )
//                .then();
        return null;
    }

}
