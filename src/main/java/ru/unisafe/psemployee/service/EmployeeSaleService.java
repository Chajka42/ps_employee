package ru.unisafe.psemployee.service;

import reactor.core.publisher.Mono;
import ru.unisafe.psemployee.dto.request.*;
import ru.unisafe.psemployee.dto.response.BaseResponse;
import ru.unisafe.psemployee.dto.response.BlockSaleResponseDto;
import ru.unisafe.psemployee.dto.response.MegafonTariffResponse;
import ru.unisafe.psemployee.dto.response.SaleResponse;

public interface EmployeeSaleService {
    Mono<SaleResponse> getSaleJson(RequestWithStationLogin requestDto);

    Mono<BlockSaleResponseDto> blockSale(BlockSaleRequest requestDto);

    Mono<BaseResponse> addSale(AddSaleRequest requestDto);

    Mono<MegafonTariffResponse> searchMegafonTariffSale(MegafonTariffRequest megafonTariffRequest);

    Mono<BaseResponse> updatePhoneInMegafonTariffSale(MegafonTariffUpdatePhoneRequest megafonTariffUpdatePhoneRequest);
}
