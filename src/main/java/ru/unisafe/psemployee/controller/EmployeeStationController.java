package ru.unisafe.psemployee.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ru.unisafe.psemployee.dto.request.RequestWithStationLogin;
import ru.unisafe.psemployee.dto.response.CouponsInfoResponse;
import ru.unisafe.psemployee.service.EmployeeStationService;

@RequiredArgsConstructor
@RequestMapping("/employee/api/stations")
@RestController
public class EmployeeStationController {

    private final EmployeeStationService employeeStationService;

    @PostMapping("/getCouponsInfo")
    public Mono<CouponsInfoResponse> getCouponsInfo(@RequestBody RequestWithStationLogin request) {
        return employeeStationService.getCouponsInfo(request);
    }
}
