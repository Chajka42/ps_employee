package ru.unisafe.psemployee.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@Table("station_notes")
public class StationNote {
    @Id
    private Long id;
    private Boolean isActive;
    private Integer employeeId;
    private String employeeName;
    private String note;
    private String login;
    private LocalDateTime created;
}
