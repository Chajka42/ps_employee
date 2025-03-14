package ru.unisafe.psemployee.service;

import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

public interface EmployeeSaleHandler {
    Mono<String> getSaleJson(String login);
}
