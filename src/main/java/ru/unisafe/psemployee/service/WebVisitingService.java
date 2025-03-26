package ru.unisafe.psemployee.service;

import reactor.core.publisher.Mono;
import ru.unisafe.psemployee.model.WebVisiting;

public interface WebVisitingService {
    Mono<WebVisiting> findWebVisitingById(Long id);
}
