package ru.unisafe.psemployee.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.unisafe.psemployee.dto.Employee;

import java.util.List;
import java.util.Map;

public interface EmployeeService {
    Flux<Employee> getAllEmployees();

    Mono<Employee> getEmployeeById(int id);

    Mono<String> checkPassword(Map<String, List<String>> queryParams);

    Mono<String> tokenCheck(Map<String, List<String>> queryParams);

    Mono<String> processLocationUpdate(String token, String lat, String lon);

    Mono<String> tokenGet(String oldToken);
}
