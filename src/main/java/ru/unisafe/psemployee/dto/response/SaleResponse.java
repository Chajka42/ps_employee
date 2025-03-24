package ru.unisafe.psemployee.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.unisafe.psemployee.dto.SaleDto;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaleResponse {
    private boolean success;
    private String login;
    private String code;
    private Integer partnerId;
    private String partner;
    private List<SaleDto> activeSaleData;
    private List<SaleDto> blockSaleData;
}
