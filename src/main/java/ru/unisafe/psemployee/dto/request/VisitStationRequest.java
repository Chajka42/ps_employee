package ru.unisafe.psemployee.dto.request;

import lombok.Data;
import ru.unisafe.psemployee.dto.StoreItemDto;

import java.util.List;

@Data
public class VisitStationRequest {
    private String login;
    private String code;
    private String address;
    private String comment;
    private int partnerId;
    private String partnerName;
    private String token;
    private boolean problemSolved;
    private String problemDescription;
    private boolean defectGathering;
    private double lat;
    private double lon;
    private int distance;
    private String problemOrigin;
    private List<StoreItemDto> dataList;
}
