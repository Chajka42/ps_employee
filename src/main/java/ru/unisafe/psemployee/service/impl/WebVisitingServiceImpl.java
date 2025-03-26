package ru.unisafe.psemployee.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.unisafe.psemployee.model.WebVisiting;
import ru.unisafe.psemployee.repository.r2dbc.WebVisitingRepository;
import ru.unisafe.psemployee.service.WebVisitingService;

@Slf4j
@RequiredArgsConstructor
@Service
public class WebVisitingServiceImpl implements WebVisitingService {

    private final WebVisitingRepository webVisitingRepository;

    @Override
    public Mono<WebVisiting> findWebVisitingById(Long id) {
        return webVisitingRepository.findById(id);
    }
}
