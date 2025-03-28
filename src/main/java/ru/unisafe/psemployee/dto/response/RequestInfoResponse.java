package ru.unisafe.psemployee.dto.response;

import lombok.Data;
import ru.unisafe.psemployee.dto.*;

import java.util.List;

@Data
public class RequestInfoResponse {
    private boolean success;
    private List<WebRequestDto> request;
    private List<CategoryDto> categories;
    /**
     * itemsTo находятся по флагу isToTT = true
     */
    private List<WebItemDto> itemsTo;
    /**
     * itemsFrom находятся по флагу isToTT = false
     */
    private List<WebItemDto> itemsFrom;
    private List<OtherRequestDto> otherRequests;
    private List<ItemRequestBeforeDto> requestedBefore;
}
