package ru.unisafe.psemployee.service;

public interface StationUtilsService {
    String generateRandomToken();

    String generateRandomKey();

    String generateMasterKeyNewOne(String stationKey, int who);
}
