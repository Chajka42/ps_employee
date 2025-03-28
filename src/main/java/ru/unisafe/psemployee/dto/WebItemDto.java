package ru.unisafe.psemployee.dto;

import lombok.Data;

@Data
public class WebItemDto {
    private long id;
    private boolean isToTt;
    private long itemId;
    private String itemName;
    private String itemTeg;
    private int itemValue;
}
