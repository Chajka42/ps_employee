package ru.unisafe.psemployee.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ru.unisafe.psemployee.dto.request.ChangeCouponsRequest;
import ru.unisafe.psemployee.dto.request.ChangeStationStoreRequest;
import ru.unisafe.psemployee.dto.request.RequestWithStationLogin;
import ru.unisafe.psemployee.dto.response.BaseResponse;
import ru.unisafe.psemployee.dto.response.CouponsInfoResponse;
import ru.unisafe.psemployee.service.EmployeeStationService;

@RequiredArgsConstructor
@RequestMapping("/employee/api/stations")
@RestController
public class EmployeeStationController {

    private final EmployeeStationService employeeStationService;

    @PostMapping("/getCouponsInfo")
    public Mono<CouponsInfoResponse> getCouponsInfo(@Validated  @RequestBody RequestWithStationLogin request) {
        return employeeStationService.getCouponsInfo(request);
    }

    @PostMapping("/changeCoupons")
    public Mono<BaseResponse> changeCoupons(@Validated @RequestBody ChangeCouponsRequest changeCouponsRequest) {
        return employeeStationService.changeCoupons(changeCouponsRequest);
    }

    @PostMapping("/changeStationStore")
    public Mono<BaseResponse> changeStationStore(@Validated @RequestBody ChangeStationStoreRequest request) {
        return employeeStationService.changeStationStore(request);
    }
}
