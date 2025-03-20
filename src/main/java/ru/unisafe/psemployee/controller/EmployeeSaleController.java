package ru.unisafe.psemployee.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.unisafe.psemployee.dto.request.*;
import ru.unisafe.psemployee.dto.response.BaseResponse;
import ru.unisafe.psemployee.dto.response.BlockSaleResponseDto;
import ru.unisafe.psemployee.dto.response.MegafonTariffResponse;
import ru.unisafe.psemployee.dto.response.SaleResponseDto;
import ru.unisafe.psemployee.service.EmployeeSaleHandler;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/employee/api")
@RestController
public class EmployeeSaleController {

    private final EmployeeSaleHandler employeeSaleHandler;

    @PostMapping("/getSaleJson")
    public Mono<SaleResponseDto> getSaleJson(@Validated @RequestBody SaleRequestDto requestDto) {
        return employeeSaleHandler.getSaleJson(requestDto);
    }

    @PostMapping("/blockSale")
    public Mono<BlockSaleResponseDto> blockSale(@Validated @RequestBody BlockSaleDto blockSaleDto) {
        return employeeSaleHandler.blockSale(blockSaleDto);
    }

    @PostMapping("/addSale")
    public Mono<BaseResponse> addSale(@Validated @RequestBody AddSaleRequest requestDto) {
        return employeeSaleHandler.addSale(requestDto);
    }

    @PostMapping("/searchMegafonTariffSale")
    public Mono<MegafonTariffResponse> searchMegafonTariffSale(@Validated @RequestBody MegafonTariffRequest megafonTariffRequest) {
        return employeeSaleHandler.searchMegafonTariffSale(megafonTariffRequest);
    }

    @PostMapping("/updateMegafonTariffSalePhone")
    public Mono<BaseResponse> updatePhoneInMegafonTariffSale(@Validated @RequestBody MegafonTariffUpdatePhoneRequest megafonTariffUpdatePhoneRequest) {
        return employeeSaleHandler.updatePhoneInMegafonTariffSale(megafonTariffUpdatePhoneRequest);
    }

}
