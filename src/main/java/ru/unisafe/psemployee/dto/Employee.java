package ru.unisafe.psemployee.dto;

public record Employee(
        Integer id,
        String token,
        String name,
        Integer access,
        Integer partnerId
) {}
