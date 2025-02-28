package ru.unisafe.psemployee.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.unisafe.psemployee.service.EmployeeService;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/employee/api")
@RestController
public class EmployeeApiController {

    private final EmployeeService employeeService;


    @PostMapping("/checkPassword")
    public Mono<ResponseEntity<?>> checkPassword(@RequestParam MultiValueMap<String, String> queryParams) {
        log.info("QueryParams: {}", queryParams);
        return employeeService.checkPassword(queryParams).map(ResponseEntity::ok);

    }

    @PostMapping("/checkToken")
    public Mono<ResponseEntity<?>> checkToken(@RequestParam MultiValueMap<String, String> queryParams) {
        log.info("QueryParams: {}", queryParams);
        return employeeService.tokenCheck(queryParams)
                .map(ResponseEntity::ok);
    }
}
