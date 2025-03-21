package ru.unisafe.psemployee.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateStationRequest {
    private String token;
    private String stationCode;
    private String address;
    private String ioInfo;
    private String comment;
    private int partnerId;
    private int unisafeRegion;
}
