package ru.unisafe.psemployee.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.unisafe.psemployee.dto.StationFilterDto;
import ru.unisafe.psemployee.dto.StationRecord;
import ru.unisafe.psemployee.dto.request.*;
import ru.unisafe.psemployee.dto.response.BaseResponse;
import ru.unisafe.psemployee.dto.response.CouponsInfoResponse;

public interface EmployeeStationService {
    Mono<CouponsInfoResponse> getCouponsInfo(RequestWithStationLogin request);

    Mono<BaseResponse> changeCoupons(ChangeCouponsRequest request);

    Mono<BaseResponse> changeStationStore(ChangeStationStoreRequest request);

    Mono<BaseResponse> createStation(CreateStationRequest request);

    Flux<StationRecord> findStations(StationFilterDto filter);
}
