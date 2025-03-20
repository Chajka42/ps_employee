package ru.unisafe.psemployee.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ru.unisafe.psemployee.dto.request.StatisticsExcelRequest;
import ru.unisafe.psemployee.service.EmployeeStatisticsService;

@RequiredArgsConstructor
@RequestMapping("/employee/api/statistics")
@RestController
public class EmployeeStatisticsController {

    private final EmployeeStatisticsService employeeStatisticsService;

    @PostMapping("/getStationStatExcel")
    public Mono<ResponseEntity<byte[]>> getStatisticsExcel(@Validated @RequestBody StatisticsExcelRequest request) {
        return employeeStatisticsService.getStatisticsExcel(request);
    }

}
