package ru.unisafe.psemployee.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record StatisticRecord(
        Boolean defect,
        Boolean isLearning,
        Boolean isDebugging,
        Boolean isGuarantee,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy HH:mm:ss", locale = "ru")
        LocalDateTime moscowTime,
        String timeZone,
        Integer designType,
        String designId,
        String designParent,
        String designPercent,
        String elementId,
        String filmType,
        String geoLocation,
        String sale
) {}
