package ru.unisafe.psemployee.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.unisafe.psemployee.service.StationUtilsService;

import java.security.SecureRandom;
import java.util.Random;

@Slf4j
@Service
public class StationUtilsServiceImpl implements StationUtilsService {


    private static final String ALLOWED_SYMBOLS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final String ALLOWED_SYMBOLS_TOKEN = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_=+";
    private static final String SYMBOLS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final Random RANDOM = new SecureRandom();

    @Override
    public String generateRandomToken() {
        StringBuilder keyBuilder = new StringBuilder();
        for (int i = 0; i < 40; i++) {
            int randomIndex = RANDOM.nextInt(ALLOWED_SYMBOLS_TOKEN.length());
            keyBuilder.append(ALLOWED_SYMBOLS_TOKEN.charAt(randomIndex));
        }
        return keyBuilder.toString();
    }

    @Override
    public String generateRandomKey() {
        StringBuilder keyBuilder = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            int randomIndex = RANDOM.nextInt(ALLOWED_SYMBOLS.length());
            keyBuilder.append(ALLOWED_SYMBOLS.charAt(randomIndex));
        }
        return keyBuilder.toString();
    }

    @Override
    public String generateMasterKeyNewOne(String stationKey, int who) {
        if (stationKey == null || stationKey.length() != 6) {
            throw new IllegalArgumentException("Input string must be 6 characters long");
        }

        Random random = new Random();
        char[] keyChars = new char[6];
        int matchingCount = 0;

        for (int i = 0; i < 6; i++) {
            char randomSymbol = SYMBOLS.charAt(random.nextInt(SYMBOLS.length()));
            keyChars[i] = randomSymbol;
            if (stationKey.charAt(i) == randomSymbol) {
                matchingCount++;
            }
        }

        while (matchingCount != 2) {
            matchingCount = 0;

            for (int i = 0; i < 6; i++) {
                char randomSymbol = SYMBOLS.charAt(random.nextInt(SYMBOLS.length()));
                keyChars[i] = randomSymbol;
                if (stationKey.charAt(i) == randomSymbol) {
                    matchingCount++;
                }
            }
        }

        return 122 + new String(keyChars) + who;
    }
}
