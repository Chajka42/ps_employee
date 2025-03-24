package ru.unisafe.psemployee.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.unisafe.psemployee.dto.StationInformation;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StationInfoResponse {
    private String message;
    private boolean success;
    private StationInformation stationInfo;
}
