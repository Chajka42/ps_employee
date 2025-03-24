package ru.unisafe.psemployee.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.unisafe.psemployee.model.StationInfo;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StationInfoResponse {
    private String message;
    private boolean success;
    private StationInfo stationInfo;
}
