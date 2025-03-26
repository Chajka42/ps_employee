package ru.unisafe.psemployee.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ru.unisafe.psemployee.model.WebVisiting;
import ru.unisafe.psemployee.service.WebVisitingService;

@RequiredArgsConstructor
@RequestMapping("/employee/api/visits")
@RestController
public class EmployeeVisitingController {

    private final WebVisitingService webVisitingService;

    @GetMapping("/getVisitInfo")
    public Mono<WebVisiting> findWebVisitingById(@RequestParam Long id){
        return webVisitingService.findWebVisitingById(id);
    }
}
