package ru.unisafe.psemployee.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ru.unisafe.psemployee.dto.request.AddWebItemRequest;
import ru.unisafe.psemployee.dto.response.BaseResponse;
import ru.unisafe.psemployee.service.WebItemService;

@RequiredArgsConstructor
@RequestMapping("/employee/api/webItems")
@RestController
public class EmployeeWebItemsController {

    private final WebItemService webItemService;

    @PostMapping()
    public Mono<BaseResponse> addRequestItem(@Validated @RequestBody AddWebItemRequest request) {
        return webItemService.addRequestItem(request);
    }
}
