package ru.unisafe.psemployee.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class RequestDto {
    private int requestId;
    private int visorId;
    private String visorName;
    private LocalDateTime created;
    private String directionType;
    private String agregatorType;
    private boolean isCompleted;
    private boolean isBoxed;
    private String sdekId;
}
