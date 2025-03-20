package ru.unisafe.psemployee.service;

import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;
import ru.unisafe.psemployee.dto.request.StatisticsExcelRequest;

public interface EmployeeStatisticsService {
    Mono<ResponseEntity<byte[]>> getStatisticsExcel(StatisticsExcelRequest request);
}
