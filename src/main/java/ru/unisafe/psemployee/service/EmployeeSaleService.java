package ru.unisafe.psemployee.service;

import reactor.core.publisher.Mono;
import ru.unisafe.psemployee.dto.request.*;
import ru.unisafe.psemployee.dto.response.BaseResponse;
import ru.unisafe.psemployee.dto.response.BlockSaleResponseDto;
import ru.unisafe.psemployee.dto.response.MegafonTariffResponse;
import ru.unisafe.psemployee.dto.response.SaleResponseDto;

public interface EmployeeSaleService {
    Mono<SaleResponseDto> getSaleJson(SaleRequestDto requestDto);

    Mono<BlockSaleResponseDto> blockSale(BlockSaleDto requestDto);

    Mono<BaseResponse> addSale(AddSaleRequest requestDto);

    Mono<MegafonTariffResponse> searchMegafonTariffSale(MegafonTariffRequest megafonTariffRequest);

    Mono<BaseResponse> updatePhoneInMegafonTariffSale(MegafonTariffUpdatePhoneRequest megafonTariffUpdatePhoneRequest);
}
