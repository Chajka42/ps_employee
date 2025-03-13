package ru.unisafe.psemployee.service;

import reactor.core.publisher.Mono;
import ru.unisafe.psemployee.dto.response.BaseResponse;

import java.util.List;
import java.util.Map;

public interface EmployeeAuthService {

    Mono<BaseResponse> checkPassword(Map<String, List<String>> queryParams);

    Mono<BaseResponse> checkToken(Map<String, List<String>> queryParams);

}
