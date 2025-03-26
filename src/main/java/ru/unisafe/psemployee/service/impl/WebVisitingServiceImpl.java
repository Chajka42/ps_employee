package ru.unisafe.psemployee.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.unisafe.psemployee.dto.*;
import ru.unisafe.psemployee.dto.request.VisitStationRequest;
import ru.unisafe.psemployee.dto.response.BaseResponse;
import ru.unisafe.psemployee.dto.response.StationStrVstRstResponse;
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
    private final WebItemRepository webItemRepository;

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
                        .visiting(tuple.getT1().stream().map(this::toVisitingDto).toList())
                        .stationSummary(tuple.getT2().stream().findFirst().map(this::toStationSummaryDto).orElse(null))
                        .requests(tuple.getT3().stream().map(this::toRequestDto).toList())
                        .notes(tuple.getT4().stream().map(this::toNoteDto).toList())
                        .success(true)
                        .build())
                .onErrorResume(e -> {
                    log.error("Ошибка при загрузке данных для station login={}", login, e);
                    return Mono.just(StationStrVstRstResponse.builder().success(false).build());
                });
    }

    private VisitingDto toVisitingDto(WebVisiting webVisiting) {
        VisitingDto visitingDto = new VisitingDto();
        visitingDto.setRequestId(webVisiting.getId());
        visitingDto.setVisorId(webVisiting.getVisorId());
        visitingDto.setVisorName(webVisiting.getVisorName());
        visitingDto.setComment(webVisiting.getComment());
        visitingDto.setCreated(webVisiting.getCreated());
        visitingDto.setDefectGathering(webVisiting.getIsDefectGathering());
        visitingDto.setProblemSolved(webVisiting.getIsProblemSolved());
        visitingDto.setProblemDescription(webVisiting.getProblemDescription());
        return visitingDto;
    }

    private StationSummaryDto toStationSummaryDto(Store store) {
        return StationSummaryDto.builder()
                .telGloss(store.getTelGloss())
                .telMat(store.getTelMat())
                .telSpy(store.getTelSpy())
                .telColor(store.getTelColor())
                .lapColor(store.getLapColor())
                .lapGloss(store.getLapGloss())
                .lapMat(store.getLapMat())
                .watColor(store.getWatColor())
                .watGloss(store.getWatGloss())
                .watMat(store.getWatMat())
                .telSum(store.getTelGloss() + store.getTelMat() + store.getTelSpy() + store.getTelColor())
                .lapSum(store.getLapColor() + store.getLapGloss() + store.getLapMat())
                .watSum(store.getWatColor() + store.getWatGloss() + store.getWatMat())
                .defTelGloss(store.getDefTelGloss())
                .defTelMat(store.getDefTelMat())
                .defTelSpy(store.getDefTelSpy())
                .defTelColor(store.getDefTelColor())
                .defLapColor(store.getDefLapColor())
                .defLapGloss(store.getDefLapGloss())
                .defLapMat(store.getDefLapMat())
                .defWatColor(store.getDefWatColor())
                .defWatGloss(store.getDefWatGloss())
                .defWatMat(store.getDefWatMat())
                .defTotal(store.getDefTelGloss() + store.getDefTelMat() + store.getDefTelSpy() + store.getDefTelColor() +
                        store.getDefLapColor() + store.getDefLapGloss() + store.getDefLapMat() +
                        store.getDefWatColor() + store.getDefWatGloss() + store.getDefWatMat())
                .build();
    }

    private RequestDto toRequestDto(WebRequest webRequest) {
        return RequestDto.builder()
                .requestId(webRequest.getId())
                .agregatorType(webRequest.getAgregatorType())
                .created(webRequest.getCreated())
                .directionType(webRequest.getDirectionType())
                .isBoxed(webRequest.getIsBoxed())
                .isCompleted(webRequest.getIsCompleted())
                .sdekId(webRequest.getSdekId())
                .visorId(webRequest.getVisorId())
                .visorName(webRequest.getVisorName())
                .build();
    }

    private NoteDto toNoteDto(StationNote stationNote) {
        return NoteDto.builder()
                .note(stationNote.getNote())
                .created(stationNote.getCreated())
                .employeeName(stationNote.getEmployeeName())
                .id(stationNote.getId())
                .build();
    }

    //TODO оттестировать этот пиздец
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
                .map(tuple -> toWebVisiting(request, tuple.getT1(), tuple.getT2(), tuple.getT3()))
                .flatMap(webVisitingRepository::save);

        Mono<Boolean> insertItems = createVisit.flatMap(visit -> {
            if (request.getDataList() != null && !request.getDataList().isEmpty() && Objects.nonNull(visit.getId())) {
                return Flux.fromIterable(request.getDataList())
                        .map(webItem -> {
                            WebItem wi = new WebItem();
                            wi.setRequestId(0);
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
                        .map(webItemRepository::saveAll) // saveAll уже возвращает Mono<Void>
                        .then(Mono.just(true));
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

            return questRepository.updateQuestProgress(visorId, isUpdate, isNoCuts, isDefect, isPlan, isFrod);
        })
                : Mono.empty();

        return resetDefects
                .then(updateProblemDate)
                .then(createVisit)
                .flatMap(visit -> insertItems)
                .then(updateQuestProgress)
                .thenReturn(new BaseResponse(true, "Визит успешно создан"))
                .onErrorResume(e -> Mono.just(new BaseResponse(false, "Ошибка сервера: " + e.getMessage())));
    }

    private WebVisiting toWebVisiting(VisitStationRequest request, String visorName, Integer visorId, String visitingAddress) {
        return WebVisiting.builder()
                .login(request.getLogin())
                .stationCode(request.getCode())
                .visorId(visorId)
                .visorName(visorName)
                .address(request.getAddress())
                .partnerId(request.getPartnerId())
                .partnerName(request.getPartnerName())
                .created(LocalDateTime.now())
                .lat(request.getLat())
                .lon(request.getLon())
                .visitAddress(visitingAddress)
                .isDefectGathering(request.isDefectGathering())
                .isProblemSolved(request.isProblemSolved())
                .problemDescription(request.getProblemDescription())
                .comment(request.getComment())
                .build();
    }
}
