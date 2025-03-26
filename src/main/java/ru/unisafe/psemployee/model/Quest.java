package ru.unisafe.psemployee.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table("quests")
public class Quest {
    @Id
    private Integer id;
    private Integer visorId;
    private String visorName;
    private Integer totalVisits;
    private Integer totalVisitsTarget;
    private Integer updates;
    private Integer updatesTarget;
    private Integer noCuts;
    private Integer noCutsTarget;
    private Integer noPlan;
    private Integer noPlanTarget;
    private Integer frod;
    private Integer frodTarget;
    private Integer defect;
    private Integer defectTarget;
    private LocalDateTime date;
}
