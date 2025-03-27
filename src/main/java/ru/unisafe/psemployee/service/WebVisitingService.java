package ru.unisafe.psemployee.service;

import org.springframework.http.server.reactive.ServerHttpRequest;
import reactor.core.publisher.Mono;
import ru.unisafe.psemployee.dto.request.AddStationNoteRequest;
import ru.unisafe.psemployee.dto.request.DisableNoteRequest;
import ru.unisafe.psemployee.dto.request.VisitStationRequest;
import ru.unisafe.psemployee.dto.response.BaseResponse;
import ru.unisafe.psemployee.dto.response.StationStrVstRstResponse;
import ru.unisafe.psemployee.model.WebVisiting;

public interface WebVisitingService {
    Mono<WebVisiting> findWebVisitingById(Long id);

    Mono<StationStrVstRstResponse> getStationStrVstRst(String login);

    Mono<BaseResponse> createVisit(VisitStationRequest request);

    Mono<BaseResponse> addNote(AddStationNoteRequest request);

    Mono<BaseResponse> disableNote(DisableNoteRequest request);
}
