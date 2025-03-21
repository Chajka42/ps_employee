package ru.unisafe.psemployee.dto;

import java.time.LocalDateTime;

public record StationRecord(
        String login,
        String stationCode,
        int status,
        String address,
        boolean isProblem,
        String problemDescription,
        LocalDateTime problemSolvedDate,
        String partnerName
) {}
