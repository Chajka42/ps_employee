package ru.unisafe.psemployee.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import ru.unisafe.psemployee.model.WebRequest;
import ru.unisafe.psemployee.service.WebRequestService;

@RequiredArgsConstructor
@RequestMapping("/employee/api/webRequests")
@RestController
public class EmployeeWebRequestsController {

    private final WebRequestService webVisitingService;

    @GetMapping("/getReceivingList")
    public Flux<WebRequest> getReceivingList(@RequestParam String searchParam) {
        return webVisitingService.getReceivingList(searchParam);
    }
}
