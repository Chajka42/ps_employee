package ru.unisafe.psemployee.service;

import reactor.core.publisher.Mono;
import ru.unisafe.psemployee.dto.request.RequestWithStationLogin;
import ru.unisafe.psemployee.dto.response.CouponsInfoResponse;

public interface EmployeeStationService {
    Mono<CouponsInfoResponse> getCouponsInfo(RequestWithStationLogin request);
}
