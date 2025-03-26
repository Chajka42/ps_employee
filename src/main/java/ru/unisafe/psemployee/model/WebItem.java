package ru.unisafe.psemployee.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WebItem {
    private Long id;
    private int requestId;
    private boolean isToTt;
    private int itemId;
    private String itemName;
    private String itemTeg;
    private String login;
    private int itemValue;
    private Long visitId;
}
