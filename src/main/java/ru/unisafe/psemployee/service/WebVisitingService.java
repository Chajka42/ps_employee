package ru.unisafe.psemployee.service;

import reactor.core.publisher.Mono;
import ru.unisafe.psemployee.dto.response.StationStrVstRstResponse;
import ru.unisafe.psemployee.model.WebVisiting;

public interface WebVisitingService {
    Mono<WebVisiting> findWebVisitingById(Long id);

    Mono<StationStrVstRstResponse> getStationStrVstRst(String login);
}
