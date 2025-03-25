package ru.unisafe.psemployee.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.unisafe.psemployee.dto.request.GeoStatRequest;
import ru.unisafe.psemployee.dto.response.BaseResponse;
import ru.unisafe.psemployee.model.GeoMark;
import ru.unisafe.psemployee.repository.EmployeeRepositoryJOOQ;
import ru.unisafe.psemployee.repository.r2dbc.GeoMarksRepository;
import ru.unisafe.psemployee.service.GeoMarksService;

@Slf4j
@RequiredArgsConstructor
@Service
public class GeoMarksServiceImpl implements GeoMarksService {

    private final GeoMarksRepository geoMarksRepository;
    private final EmployeeRepositoryJOOQ employeeRepository;

    @Override
    public Mono<BaseResponse> saveGeoStat(GeoStatRequest request) {
        GeoMark geoMark = new GeoMark();
        geoMark.setLat(request.getLat());
        geoMark.setLon(request.getLon());
        geoMark.setLogin(request.getLogin());
        var token = request.getToken();

        return employeeRepository.getEmployeeName(token)
                .zipWith(employeeRepository.getEmployeeOriginId(token))
                .doOnNext(tuple -> log.info("Получены данные сотрудника: имя = {}, visorId = {}", tuple.getT1(), tuple.getT2()))
                .flatMap(tuple -> {
                    String employeeName = tuple.getT1();
                    int visorId = tuple.getT2();
                    geoMark.setEmployeeName(employeeName);
                    geoMark.setEmployeeId(visorId);

                    return geoMarksRepository.save(geoMark)
                            .map(saved -> new BaseResponse(true, "Geo mark saved successfully"))
                            .onErrorResume(ex -> {
                                log.error("Ошибка при сохранении GeoMark: {}", ex.getMessage(), ex);
                                return Mono.just(new BaseResponse(false, "Ошибка при сохранении GeoMark: " + ex.getMessage()));
                            });
                });


    }
}
