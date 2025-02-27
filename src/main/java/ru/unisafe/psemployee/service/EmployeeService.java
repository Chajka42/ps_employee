package ru.unisafe.psemployee.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.unisafe.psemployee.dto.Employee;

public interface EmployeeService {
    Flux<Employee> getAllEmployees();

    Mono<Employee> getEmployeeById(int id);
}
