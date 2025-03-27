package ru.unisafe.psemployee.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.unisafe.psemployee.dto.request.*;
import ru.unisafe.psemployee.dto.response.BaseResponse;
import ru.unisafe.psemployee.dto.response.BlockSaleResponseDto;
import ru.unisafe.psemployee.dto.response.MegafonTariffResponse;
import ru.unisafe.psemployee.dto.response.SaleResponse;
import ru.unisafe.psemployee.service.EmployeeSaleService;

@RequiredArgsConstructor
@RequestMapping("/employee/api")
@RestController
public class EmployeeSaleController {

    private final EmployeeSaleService employeeSaleService;

    @PostMapping("/getSaleJson")
    public Mono<SaleResponse> getSaleJson(@Validated @RequestBody RequestWithStationLogin requestDto) {
        return employeeSaleService.getSaleJson(requestDto);
    }

    @PostMapping("/blockSale")
    public Mono<BlockSaleResponseDto> blockSale(@Validated @RequestBody BlockSaleRequest blockSaleRequest) {
        return employeeSaleService.blockSale(blockSaleRequest);
    }

    //TODO Не работает {"error":"invalid_grant","error_description":"Invalid JWT Signature."} при попытке обращения к Firebase
    @PostMapping("/addSale")
    public Mono<BaseResponse> addSale(@Validated @RequestBody AddSaleRequest requestDto) {
        return employeeSaleService.addSale(requestDto);
    }

    @PostMapping("/searchMegafonTariffSale")
    public Mono<MegafonTariffResponse> searchMegafonTariffSale(@Validated @RequestBody MegafonTariffRequest megafonTariffRequest) {
        return employeeSaleService.searchMegafonTariffSale(megafonTariffRequest);
    }

    @PostMapping("/updateMegafonTariffSalePhone")
    public Mono<BaseResponse> updatePhoneInMegafonTariffSale(@Validated @RequestBody MegafonTariffUpdatePhoneRequest megafonTariffUpdatePhoneRequest) {
        return employeeSaleService.updatePhoneInMegafonTariffSale(megafonTariffUpdatePhoneRequest);
    }

}
