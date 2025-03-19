package ru.unisafe.psemployee.service.impl;

import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.unisafe.psemployee.service.NotificationService;

@Slf4j
@RequiredArgsConstructor
@Service
public class NotificationServiceImpl implements NotificationService {

    private final FirebaseMessaging firebaseMessaging;

    public Mono<String> sendNotification(String key, String value, String deviceToken) {

        log.info("Sending notification");
        log.info("key: {}", key);
        log.info("value: {}", value);
        log.info("deviceToken: {}", deviceToken);

        Message message = Message.builder()
                .putData(key, value)
                .setToken(deviceToken)
                .setAndroidConfig(AndroidConfig.builder()
                        .setPriority(AndroidConfig.Priority.HIGH)
                        .build())
                .build();

        //TODO пишет "error_description":"Invalid JWT Signature." iss: firebase-adminsdk-fpvj7@protection-station-2.iam.gserviceaccount.com
        return Mono.fromCallable(() -> firebaseMessaging.send(message))
                .onErrorResume(e -> Mono.just("ERROR: " + e.getMessage()));
    }
}
