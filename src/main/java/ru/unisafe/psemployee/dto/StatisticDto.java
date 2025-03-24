package ru.unisafe.psemployee.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class StatisticDto {
    private Integer id;
    private String moscowTime;
    private String timeZone;
    private Integer designType;
    private String designId;
    private String designPercent;
    private String designParent;
    private Integer elementId;
    private String type;
}
