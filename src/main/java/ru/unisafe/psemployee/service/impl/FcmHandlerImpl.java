package ru.unisafe.psemployee.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.unisafe.psemployee.repository.FirebaseRepositoryJOOQ;
import ru.unisafe.psemployee.service.FcmHandler;
import ru.unisafe.psemployee.service.NotificationService;


@Slf4j
@RequiredArgsConstructor
@Service
public class FcmHandlerImpl implements FcmHandler {

    private final NotificationService notificationService;
    private final FirebaseRepositoryJOOQ firebaseRepository;

    @Override
    public Mono<Void> autoUpdateSaleList(String partnerCode, int partnerId) {
        return firebaseRepository.findFcmTokenByStationCodeAndPartnerId(partnerCode, partnerId)
                .flatMap(myToken -> {
                    if (!"?".equals(myToken)) {
                        return notificationService.sendNotification("UPDATE_SALES", "", myToken)
                                .doOnNext(fcmResponse -> {
                                    if (!fcmResponse.startsWith("projects/protection-station-2")) {
                                        log.info(fcmResponse);
                                    }
                                })
                                .then();
                    } else {
                        log.info("Auto check error - Partner code: {}, partner_id: {}", partnerCode, partnerId);
                        return Mono.empty();
                    }
                })
                .onErrorResume(e -> {
                    log.error("Ошибка при обновлении списка продаж", e);
                    return Mono.empty();
                });
    }
}
