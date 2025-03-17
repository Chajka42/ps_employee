package ru.unisafe.psemployee.service;

import reactor.core.publisher.Mono;
import ru.unisafe.psemployee.dto.response.BlockSaleResponseDto;
import ru.unisafe.psemployee.dto.response.SaleDtoResponse;

import java.util.List;
import java.util.Map;

public interface EmployeeSaleHandler {
    Mono<SaleDtoResponse> getSaleJson(String login);

    Mono<BlockSaleResponseDto> blockSale(Integer partnerId, Integer id);
}
