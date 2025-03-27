package ru.unisafe.psemployee.mapper;

import org.springframework.stereotype.Component;
import ru.unisafe.psemployee.dto.NoteDto;
import ru.unisafe.psemployee.dto.RequestDto;
import ru.unisafe.psemployee.dto.StationSummaryDto;
import ru.unisafe.psemployee.dto.VisitingDto;
import ru.unisafe.psemployee.dto.request.VisitStationRequest;
import ru.unisafe.psemployee.model.StationNote;
import ru.unisafe.psemployee.model.Store;
import ru.unisafe.psemployee.model.WebRequest;
import ru.unisafe.psemployee.model.WebVisiting;

import java.time.LocalDateTime;

public class WebVisitingMapper {

    public static VisitingDto toVisitingDto(WebVisiting webVisiting) {
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

    public static StationSummaryDto toStationSummaryDto(Store store) {
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

    public static RequestDto toRequestDto(WebRequest webRequest) {
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

    public static NoteDto toNoteDto(StationNote stationNote) {
        return NoteDto.builder()
                .note(stationNote.getNote())
                .created(stationNote.getCreated())
                .employeeName(stationNote.getEmployeeName())
                .id(stationNote.getId())
                .build();
    }

    public static WebVisiting toWebVisiting(VisitStationRequest request, String visorName, Integer visorId, String visitingAddress) {
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
