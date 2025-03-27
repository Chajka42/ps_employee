package ru.unisafe.psemployee.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.unisafe.psemployee.dto.request.AddStationNoteRequest;
import ru.unisafe.psemployee.dto.request.DisableNoteRequest;
import ru.unisafe.psemployee.dto.request.VisitStationRequest;
import ru.unisafe.psemployee.dto.response.BaseResponse;
import ru.unisafe.psemployee.dto.response.StationStrVstRstResponse;
import ru.unisafe.psemployee.mapper.WebVisitingMapper;
import ru.unisafe.psemployee.model.*;
import ru.unisafe.psemployee.repository.EmployeeRepositoryJOOQ;
import ru.unisafe.psemployee.repository.QuestRepository;
import ru.unisafe.psemployee.repository.r2dbc.*;
import ru.unisafe.psemployee.service.OpenStreetMapService;
import ru.unisafe.psemployee.service.WebVisitingService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Service
public class WebVisitingServiceImpl implements WebVisitingService {

    private final WebVisitingRepository webVisitingRepository;
    private final StoreRepository storeRepository;
    private final WebRequestRepository webRequestRepository;
    private final StationNoteRepository stationNoteRepository;
    private final TtsRepository ttsRepository;
    private final WebItemsRepository webItemsRepository;

    private final EmployeeRepositoryJOOQ employeeRepository;
    private final QuestRepository questRepository;

    private final OpenStreetMapService openStreetMapService;

    @Override
    public Mono<WebVisiting> findWebVisitingById(Long id) {
        return webVisitingRepository.findById(id);
    }

    @Override
    public Mono<StationStrVstRstResponse> getStationStrVstRst(String login) {

        Mono<List<WebVisiting>> webVisitingsMono = webVisitingRepository.findLatestByLogin(login)
                .collectList();

        Mono<List<Store>> storesMono = storeRepository.findByLogin(login)
                .collectList();

        Mono<List<WebRequest>> webRequestsMono = webRequestRepository.findLatestByLogin(login)
                .collectList();

        Mono<List<StationNote>> stationNotesMono = stationNoteRepository.findByLoginAndIsActiveTrue(login)
                .collectList();

        return Mono.zip(webVisitingsMono, storesMono, webRequestsMono, stationNotesMono)
                .map(tuple -> StationStrVstRstResponse.builder()
                        .visiting(tuple.getT1().stream().map(WebVisitingMapper::toVisitingDto).toList())
                        .stationSummary(tuple.getT2().stream().findFirst().map(WebVisitingMapper::toStationSummaryDto).orElse(null))
                        .requests(tuple.getT3().stream().map(WebVisitingMapper::toRequestDto).toList())
                        .notes(tuple.getT4().stream().map(WebVisitingMapper::toNoteDto).toList())
                        .success(true)
                        .build())
                .onErrorResume(e -> {
                    log.error("Ошибка при загрузке данных для station login={}", login, e);
                    return Mono.just(StationStrVstRstResponse.builder().success(false).build());
                });
    }

    @Override
    public Mono<BaseResponse> createVisit(VisitStationRequest request) {
        boolean isDefectGathering = request.isDefectGathering();
        boolean isProblemSolved = request.isProblemSolved();
        String login = request.getLogin();
        String token = request.getToken();

        Mono<String> visorNameMono = employeeRepository.getEmployeeName(token);
        Mono<Integer> visorIdMono = employeeRepository.getEmployeeVisorId(token);
        Mono<String> addressMono = openStreetMapService.getAddressFromCoordinates(request.getLat(), request.getLon());

        Mono<Void> resetDefects = isDefectGathering ? storeRepository.resetStoreDefects(login) : Mono.empty();
        Mono<Void> updateProblemDate = isProblemSolved
                ? ttsRepository.updateProblemSolvedDateByLogin(login, LocalDateTime.now())
                : Mono.empty();

        Mono<WebVisiting> createVisit = Mono.zip(visorNameMono, visorIdMono, addressMono)
                .map(tuple -> WebVisitingMapper.toWebVisiting(request, tuple.getT1(), tuple.getT2(), tuple.getT3()))
                .doOnNext(webVisiting -> log.info(webVisiting.toString()))
                .flatMap(webVisitingRepository::save)
                .cache();

        Mono<Boolean> insertItems = createVisit.flatMap(visit -> {
            if (request.getDataList() != null && !request.getDataList().isEmpty() && Objects.nonNull(visit.getId())) {
                return Flux.fromIterable(request.getDataList())
                        .map(webItem -> {
                            WebItem wi = new WebItem();
                            wi.setRequestId(0L);
                            wi.setToTt(true);
                            wi.setLogin(login);
                            wi.setItemId(webItem.getId());
                            wi.setItemName(webItem.getName());
                            wi.setItemTeg(webItem.getTeg());
                            wi.setItemValue(webItem.getValue());
                            wi.setVisitId(visit.getId());
                            return wi;
                        })
                        .collectList()
                        .map(webItemsRepository::saveAll)
                        .thenReturn(true)
                        .onErrorResume(e -> {
                            log.error("Ошибка при вставке элементов визита", e);
                            return Mono.just(false);
                        });
            }
            return Mono.just(false);
        });

        Mono<Void> updateQuestProgress = isProblemSolved
                ? visorIdMono.flatMap(visorId -> {
            String problem = request.getProblemOrigin();
            boolean isUpdate = problem.contains("обновление");
            boolean isNoCuts = problem.contains("резов");
            boolean isDefect = problem.contains("брака");
            boolean isPlan = problem.contains("план");
            boolean isFrod = problem.contains("фрод");

            return questRepository.updateQuestProgress(visorId, isUpdate, isNoCuts, isDefect, isPlan, isFrod)
                    .then();

        }) : Mono.empty();

        return resetDefects
                .then(updateProblemDate)
                .then(createVisit)
                .flatMap(visit -> insertItems)
                .then(updateQuestProgress)
                .thenReturn(new BaseResponse(true, "Визит успешно создан"))
                .onErrorResume(e -> {
                    log.error("Ошибка при создании визита", e);
                    return Mono.just(new BaseResponse(false, "Ошибка сервера: " + e.getMessage()));
                });
    }

    @Override
    public Mono<BaseResponse> addNote(AddStationNoteRequest request) {
        String login = request.getLogin();
        String token = request.getToken();
        String note = request.getNote();

        log.info("Добавление заметки: login={}, note={}, token={}", login, note, token);

        return employeeRepository.getEmployeeName(token)
                .zipWith(employeeRepository.getEmployeeOriginId(token))
                .flatMap(tuple -> {
                    String employeeName = tuple.getT1();
                    int employeeId = tuple.getT2();

                    log.info("Получены данные сотрудника: name={}, id={}", employeeName, employeeId);

                    StationNote noteDto = new StationNote();
                    noteDto.setLogin(login);
                    noteDto.setNote(note);
                    noteDto.setEmployeeId(employeeId);
                    noteDto.setEmployeeName(employeeName);

                    return stationNoteRepository.save(noteDto)
                            .flatMap(stationNote -> {
                                if (Objects.nonNull(stationNote.getId())) {
                                    log.info("Заметка успешно добавлена: id={}", stationNote.getId());
                                    return Mono.just(new BaseResponse(true, "Заметка успешно добавлена"));
                                } else {
                                    log.warn("Ошибка при сохранении заметки");
                                    return Mono.just(new BaseResponse(false, "Произошла ошибка при добавлении заметки"));
                                }
                            });
                })
                .onErrorResume(e -> {
                    log.error("Ошибка при добавлении заметки", e);
                    return Mono.just(new BaseResponse(false, "Ошибка сервера: " + e.getMessage()));
                });
    }

    @Override
    public Mono<BaseResponse> disableNote(DisableNoteRequest request) {
        Long noteId = request.getNoteId();

        log.info("Деактивация заметки: noteId={}", noteId);

        return stationNoteRepository.deactivateNote(noteId)
                .flatMap(note -> {
                    log.info("Заметка деактивирована: {}", note);
                    return Mono.just(new BaseResponse(true, "Заметка деактивирована успешно"));
                })
                .switchIfEmpty(Mono.just(new BaseResponse(false, "Произошла ошибка в процессе деактивации заметки")))
                .onErrorResume(e -> {
                    log.error("Ошибка при деактивации заметки", e);
                    return Mono.just(new BaseResponse(false, "Ошибка сервера: " + e.getMessage()));
                });
    }

}
