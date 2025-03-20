package ru.unisafe.psemployee.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.unisafe.psemployee.dto.request.ChangeCouponsRequest;
import ru.unisafe.psemployee.dto.request.ChangeStationStoreRequest;
import ru.unisafe.psemployee.dto.request.RequestWithStationLogin;
import ru.unisafe.psemployee.dto.response.BaseResponse;
import ru.unisafe.psemployee.dto.response.CouponsInfoResponse;
import ru.unisafe.psemployee.repository.AchievementsRepositoryJOOQ;
import ru.unisafe.psemployee.repository.StoreRepositoryJOOQ;
import ru.unisafe.psemployee.service.EmployeeStationService;

@RequiredArgsConstructor
@Slf4j
@Service
public class EmployeeStationServiceImpl implements EmployeeStationService {

    private final AchievementsRepositoryJOOQ achievementsRepository;
    private final StoreRepositoryJOOQ storeRepository;

    @Override
    public Mono<CouponsInfoResponse> getCouponsInfo(RequestWithStationLogin request) {
        var login = request.getLogin();

        return achievementsRepository.fetchCouponsInfo(login)
                .flatMap(couponsInfo ->
                        achievementsRepository.fetchCouponChangeList(login)
                                .collectList()
                                .map(changes -> {
                                    couponsInfo.setData(changes);
                                    return couponsInfo;
                                }))
                .doOnError(error -> log.error("Ошибка при получении информации о купонах для станции {}: {}", login, error.getMessage()));
    }

    @Override
    public Mono<BaseResponse> changeCoupons(ChangeCouponsRequest request) {
        return achievementsRepository.changeCoupons(request);
    }

    @Override
    public Mono<BaseResponse> changeStationStore(ChangeStationStoreRequest request) {
        return storeRepository.changeStationStore(request);
    }

}
