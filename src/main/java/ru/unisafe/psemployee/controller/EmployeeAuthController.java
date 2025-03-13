package ru.unisafe.psemployee.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.unisafe.psemployee.dto.response.BaseResponse;
import ru.unisafe.psemployee.dto.response.CheckTokenResponse;
import ru.unisafe.psemployee.dto.response.PasswordCheckResponse;
import ru.unisafe.psemployee.service.EmployeeAuthService;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/employee/api")
@RestController
public class EmployeeAuthController {

    private final EmployeeAuthService employeeAuthService;


    @Operation(summary = "Проверка пароля. Посмотри на PasswordCheckResponse - там есть изменения в ответе",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Результат проверки пароля",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(oneOf = {BaseResponse.class, PasswordCheckResponse.class})
                            )
                    )
            }
    )
    @PostMapping("/checkPassword")
    public Mono<BaseResponse> checkPassword(@RequestParam MultiValueMap<String, String> queryParams) {
        log.info("QueryParams: {}", queryParams);
        return employeeAuthService.checkPassword(queryParams);

    }

    @Operation(summary = "Проверка токена",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Результат проверки токена",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(oneOf = {BaseResponse.class, CheckTokenResponse.class})
                            )
                    )
            }
    )
    @PostMapping("/checkToken")
    public Mono<BaseResponse> checkToken(@RequestParam MultiValueMap<String, String> queryParams) {
        log.info("QueryParams: {}", queryParams);
        return employeeAuthService.checkToken(queryParams);
    }
}
