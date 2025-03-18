package ru.unisafe.psemployee.service;

import reactor.core.publisher.Mono;
import ru.unisafe.psemployee.dto.request.BlockSaleDto;
import ru.unisafe.psemployee.dto.request.SaleRequestDto;
import ru.unisafe.psemployee.dto.response.BlockSaleResponseDto;
import ru.unisafe.psemployee.dto.response.SaleResponseDto;

public interface EmployeeSaleHandler {
    Mono<SaleResponseDto> getSaleJson(SaleRequestDto requestDto);

    Mono<BlockSaleResponseDto> blockSale(BlockSaleDto requestDto);
}
