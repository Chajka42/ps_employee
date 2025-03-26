package ru.unisafe.psemployee.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.unisafe.psemployee.dto.request.VisitStationRequest;
import ru.unisafe.psemployee.dto.response.BaseResponse;
import ru.unisafe.psemployee.dto.response.StationStrVstRstResponse;
import ru.unisafe.psemployee.model.WebVisiting;
import ru.unisafe.psemployee.service.WebVisitingService;

@RequiredArgsConstructor
@RequestMapping("/employee/api/visits")
@RestController
public class EmployeeVisitingController {

    private final WebVisitingService webVisitingService;

    @GetMapping("/getVisitInfo")
    public Mono<WebVisiting> findWebVisitingById(@RequestParam Long id) {
        return webVisitingService.findWebVisitingById(id);
    }

    @GetMapping("/getStationStrVstRst")
    public Mono<StationStrVstRstResponse> getStationStrVstRst(@RequestParam String login) {
        return webVisitingService.getStationStrVstRst(login);
    }

    @PostMapping("/createVisit")
    public Mono<BaseResponse> createVisit(
            @Validated @RequestBody VisitStationRequest visitRequest) {
        return webVisitingService.createVisit(visitRequest);
    }

}
