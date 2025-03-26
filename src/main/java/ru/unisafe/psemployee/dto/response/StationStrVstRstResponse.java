package ru.unisafe.psemployee.dto.response;

import lombok.Builder;
import lombok.Data;
import ru.unisafe.psemployee.dto.NoteDto;
import ru.unisafe.psemployee.dto.RequestDto;
import ru.unisafe.psemployee.dto.StationSummaryDto;
import ru.unisafe.psemployee.dto.VisitingDto;

import java.util.List;

@Builder
@Data
public class StationStrVstRstResponse {
    private boolean success;
    private StationSummaryDto stationSummary;
    private List<RequestDto> requests;
    private List<VisitingDto> visiting;
    private List<NoteDto> notes;
}