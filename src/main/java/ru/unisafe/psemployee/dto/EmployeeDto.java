package ru.unisafe.psemployee.dto;

public record EmployeeDto(
        Integer id,
        String token,
        String name,
        Integer access,
        Integer partnerId
) {}
