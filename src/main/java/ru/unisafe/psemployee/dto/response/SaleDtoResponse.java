package ru.unisafe.psemployee.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaleDtoResponse {
    private boolean success;
    private String login;
    private String code;
    private Integer partnerId;
    private String partner;
    private List<SaleDto> activeSaleData;
    private List<SaleDto> blockSaleData;
}
