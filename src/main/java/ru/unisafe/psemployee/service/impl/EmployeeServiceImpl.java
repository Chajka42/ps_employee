package ru.unisafe.psemployee.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.unisafe.psemployee.dto.EmployeeDto;
import ru.unisafe.psemployee.repository.EmployeeRepositoryJOOQ;
import ru.unisafe.psemployee.service.EmployeeService;

@RequiredArgsConstructor
@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepositoryJOOQ employeeRepository;


    @Override
    public Flux<EmployeeDto> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public Mono<EmployeeDto> getEmployeeById(int id) {
        return employeeRepository.findById(id);
    }
}
