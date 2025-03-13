package ru.unisafe.psemployee.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.unisafe.psemployee.dto.Employee;
import ru.unisafe.psemployee.repository.EmployeeRepository;
import ru.unisafe.psemployee.service.EmployeeService;

@RequiredArgsConstructor
@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;


    @Override
    public Flux<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public Mono<Employee> getEmployeeById(int id) {
        return employeeRepository.findById(id);
    }
}
