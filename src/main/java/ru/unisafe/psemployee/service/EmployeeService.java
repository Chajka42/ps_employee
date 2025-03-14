package ru.unisafe.psemployee.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.unisafe.psemployee.dto.EmployeeDto;

public interface EmployeeService {
    Flux<EmployeeDto> getAllEmployees();

    Mono<EmployeeDto> getEmployeeById(int id);
}
