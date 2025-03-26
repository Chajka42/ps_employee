package ru.unisafe.psemployee.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class NoteDto {
    private Long id;
    private String employeeName;
    private String note;
    private LocalDateTime created;
}
