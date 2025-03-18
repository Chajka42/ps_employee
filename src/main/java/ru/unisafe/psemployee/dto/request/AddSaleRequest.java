package ru.unisafe.psemployee.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddSaleRequest {
    private String login;
    private String code;
    private int partnerId;
    private int type;
    private int model;
}
