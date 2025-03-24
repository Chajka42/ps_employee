package ru.unisafe.psemployee.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.unisafe.psemployee.model.StationInfoExtended;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StationInfoMenuResponse {
    private String message;
    private boolean success;
    private StationInfoExtended stationInfo;
}
