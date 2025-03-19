package ru.unisafe.psemployee.service;

import reactor.core.publisher.Mono;

public interface NotificationService {
    Mono<String> sendNotification(String key, String value, String deviceToken);
}
