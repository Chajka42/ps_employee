package ru.unisafe.psemployee.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.apache.poi.ss.formula.functions.T;
import ru.unisafe.psemployee.dto.StationInformation;

import java.time.ZonedDateTime;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class StationInfoExtended extends StationInfo<T> implements StationInformation {
    private int rrPlan;
    private int rrPrognosis;
    private int rrPercent;
    private int rrDynamic;
    private int rrDefects;
    private int rrCuts;

    private String actBuild;
    private String actDebuild;
    private String actFact;

    private String plotterName;

    private String newCutsSales;
    private String cleaningSales;
    private String antivirusSales;

    private int elementId;
    private String designPercent;
    private String designParent;
    private String designId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy HH:mm:ss", locale = "ru")
    private ZonedDateTime moscowTime;

    private String tel;
    private String lap;
    private String wat;
}
