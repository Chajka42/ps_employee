package ru.unisafe.psemployee.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.unisafe.psemployee.dto.request.WebRequestReceiveRequest;
import ru.unisafe.psemployee.dto.response.BaseResponse;
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

    @PostMapping("/receiveRequest")
    public Mono<BaseResponse> receiveRequest(@Validated @RequestBody WebRequestReceiveRequest request) {
        return webVisitingService.receiveRequest(request);
    }
}
