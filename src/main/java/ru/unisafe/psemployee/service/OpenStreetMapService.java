package ru.unisafe.psemployee.service;

import reactor.core.publisher.Mono;

public interface OpenStreetMapService {
    Mono<String> getAddressFromCoordinates(double latitude, double longitude);
}
