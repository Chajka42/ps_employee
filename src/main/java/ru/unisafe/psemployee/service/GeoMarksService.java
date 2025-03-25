package ru.unisafe.psemployee.service;

import reactor.core.publisher.Mono;
import ru.unisafe.psemployee.dto.request.GeoStatRequest;
import ru.unisafe.psemployee.dto.response.BaseResponse;

public interface GeoMarksService {
    Mono<BaseResponse> saveGeoStat(GeoStatRequest request);
}
