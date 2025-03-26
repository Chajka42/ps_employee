package ru.unisafe.psemployee.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.unisafe.psemployee.dto.NoteDto;
import ru.unisafe.psemployee.dto.RequestDto;
import ru.unisafe.psemployee.dto.StationSummaryDto;
import ru.unisafe.psemployee.dto.VisitingDto;
import ru.unisafe.psemployee.dto.response.StationStrVstRstResponse;
import ru.unisafe.psemployee.model.StationNote;
import ru.unisafe.psemployee.model.Store;
import ru.unisafe.psemployee.model.WebRequest;
import ru.unisafe.psemployee.model.WebVisiting;
import ru.unisafe.psemployee.repository.r2dbc.StationNoteRepository;
import ru.unisafe.psemployee.repository.r2dbc.StoreRepository;
import ru.unisafe.psemployee.repository.r2dbc.WebRequestRepository;
import ru.unisafe.psemployee.repository.r2dbc.WebVisitingRepository;
import ru.unisafe.psemployee.service.WebVisitingService;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class WebVisitingServiceImpl implements WebVisitingService {

    private final WebVisitingRepository webVisitingRepository;
    private final StoreRepository storeRepository;
    private final WebRequestRepository webRequestRepository;
    private final StationNoteRepository stationNoteRepository;

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
}
