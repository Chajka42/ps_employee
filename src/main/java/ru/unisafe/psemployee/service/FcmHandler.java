package ru.unisafe.psemployee.service;

import reactor.core.publisher.Mono;

public interface FcmHandler {
    Mono<Void> autoUpdateSaleList(String partnerCode, int partnerId);
}
