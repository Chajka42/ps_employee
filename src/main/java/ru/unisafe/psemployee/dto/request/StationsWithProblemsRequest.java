package ru.unisafe.psemployee.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StationsWithProblemsRequest {
    private String token;
    private String address;
    private String code;
    private boolean isAnyVisor;
    private int partnerId;
    private boolean isActive;
    private String problem;
}
