package ru.unisafe.psemployee.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ru.unisafe.psemployee.dto.request.GeoStatRequest;
import ru.unisafe.psemployee.dto.response.BaseResponse;
import ru.unisafe.psemployee.service.GeoMarksService;

@RequiredArgsConstructor
@Validated
@RequestMapping("/employee/api/geo")
@RestController
public class GeoController {

    private final GeoMarksService geoMarksService;

    @PostMapping("/getDistance")
    public Mono<BaseResponse> saveGeoStat(@Validated @RequestBody GeoStatRequest request) {
        return geoMarksService.saveGeoStat(request);
    }
}
