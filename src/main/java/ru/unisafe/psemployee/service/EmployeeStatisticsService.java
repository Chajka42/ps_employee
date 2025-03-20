package ru.unisafe.psemployee.service;

import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.unisafe.psemployee.dto.StatisticRecord;
import ru.unisafe.psemployee.dto.request.RequestWithStationLogin;
import ru.unisafe.psemployee.dto.request.StatisticsExcelRequest;

import java.util.List;

public interface EmployeeStatisticsService {
    Mono<ResponseEntity<byte[]>> getStatisticsExcel(StatisticsExcelRequest request);

    Flux<StatisticRecord> getStatistic(RequestWithStationLogin request);
}
