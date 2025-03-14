package ru.unisafe.psemployee.repository.r2dbc;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.unisafe.psemployee.model.Employee;

//public interface EmployeeRepository extends R2dbcRepository<Employee, Integer> {
//
//    Mono<Employee> findByToken(String token);
//    Mono<Employee> findByRelictToken(String relictToken);
//    Mono<Employee> findByPassFunction(String passFunction);
//    Mono<Boolean> existsByToken(String token);
//    Mono<Boolean> existsByRelictToken(String relictToken);
//    Flux<Employee> findByPartnerId(Integer partnerId);
//    Mono<Void> updateEmployeeByLatAndLonAndAddress(Double lat, Double lon, String address);
//}
