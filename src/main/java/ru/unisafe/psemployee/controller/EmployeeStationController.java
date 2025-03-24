package ru.unisafe.psemployee.controller;

import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.unisafe.psemployee.dto.StationFilterDto;
import ru.unisafe.psemployee.dto.StationRecord;
import ru.unisafe.psemployee.dto.request.*;
import ru.unisafe.psemployee.dto.response.BaseResponse;
import ru.unisafe.psemployee.dto.response.CouponsInfoResponse;
import ru.unisafe.psemployee.dto.response.StationInfoResponse;
import ru.unisafe.psemployee.service.EmployeeStationService;

@RequiredArgsConstructor
@RequestMapping("/employee/api/stations")
@Validated
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

    @PostMapping("/createNewStation")
    public Mono<BaseResponse> createNewStation(@Validated @RequestBody CreateStationRequest request) {
        return employeeStationService.createStation(request);
    }

    @GetMapping("/search")
    public Flux<StationRecord> searchStations(
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) Integer partnerId,
            @RequestParam(required = false) Integer visorId,
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(defaultValue = "any") String isProblem,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        StationFilterDto filter = new StationFilterDto(code, address, partnerId, visorId, isActive, isProblem, page, size);
        return employeeStationService.findStations(filter);
    }

    @GetMapping("/getStationInfo")
    public Mono<StationInfoResponse> getStationInfo(@Validated StationInfoRequest request) {
        return employeeStationService.getStationInfo(request.getLogin());
    }

    @GetMapping("/getStationInfoMenu")
    public Mono<StationInfoResponse> getStationMenuInfo(@Validated StationInfoRequest request) {
        return employeeStationService.getStationMenuInfo(request.getLogin());
    }

    @GetMapping("/getStationInfoSupport")
    public Mono<StationInfoResponse> getStationInfoSupport(@Validated StationInfoRequest request) {
        return employeeStationService.getStationInfoSupport(request.getLogin());
    }


}
