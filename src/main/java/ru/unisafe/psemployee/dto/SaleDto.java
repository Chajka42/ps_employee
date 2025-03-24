package ru.unisafe.psemployee.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaleDto {
    private Integer id;
    private Boolean wasLoaded;
    private Integer model;
    private String type;
    private String time;
    private String status;
}
