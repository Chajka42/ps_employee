package ru.unisafe.psemployee.dto;

public record StationFilterDto(
        String code,
        String address,
        Integer partnerId,
        Integer visorId,
        Boolean isActive,
        String isProblem,
        int page,
        int size
) {}
