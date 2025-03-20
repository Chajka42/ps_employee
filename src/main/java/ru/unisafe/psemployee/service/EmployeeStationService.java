package ru.unisafe.psemployee.service;

import reactor.core.publisher.Mono;
import ru.unisafe.psemployee.dto.request.ChangeCouponsRequest;
import ru.unisafe.psemployee.dto.request.ChangeStationStoreRequest;
import ru.unisafe.psemployee.dto.request.RequestWithStationLogin;
import ru.unisafe.psemployee.dto.response.BaseResponse;
import ru.unisafe.psemployee.dto.response.CouponsInfoResponse;

public interface EmployeeStationService {
    Mono<CouponsInfoResponse> getCouponsInfo(RequestWithStationLogin request);

    Mono<BaseResponse> changeCoupons(ChangeCouponsRequest request);

    Mono<BaseResponse> changeStationStore(ChangeStationStoreRequest request);
}
