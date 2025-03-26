package ru.unisafe.psemployee.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class VisitingDto {
    private Long requestId;
    private int visorId;
    private String visorName;
    private LocalDateTime created;
    private boolean isDefectGathering;
    private boolean isProblemSolved;
    private String problemDescription;
    private String comment;
}
