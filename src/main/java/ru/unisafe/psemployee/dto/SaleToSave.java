package ru.unisafe.psemployee.dto;

import java.time.LocalDateTime;

public record SaleToSave(String stationCode,
                         String stationLogin,
                         LocalDateTime date,
                         LocalDateTime serverTime,
                         String article,
                         String receipt,
                         int who,
                         int type,
                         int model) {
}
