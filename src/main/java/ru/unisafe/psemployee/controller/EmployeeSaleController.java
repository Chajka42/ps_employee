package ru.unisafe.psemployee.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.unisafe.psemployee.dto.request.AddSaleRequest;
import ru.unisafe.psemployee.dto.request.BlockSaleDto;
import ru.unisafe.psemployee.dto.request.SaleRequestDto;
import ru.unisafe.psemployee.dto.response.BlockSaleResponseDto;
import ru.unisafe.psemployee.dto.response.SaleResponseDto;
import ru.unisafe.psemployee.service.EmployeeSaleHandler;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/employee/api")
@RestController
public class EmployeeSaleController {

    private final EmployeeSaleHandler employeeSaleHandler;

    @PostMapping("/getSaleJson")
    public Mono<SaleResponseDto> getSaleJson(@RequestBody SaleRequestDto requestDto) {
        return employeeSaleHandler.getSaleJson(requestDto);
    }

    @PostMapping("/blockSale")
    public Mono<BlockSaleResponseDto> blockSale(@RequestBody BlockSaleDto blockSaleDto) {
        return employeeSaleHandler.blockSale(blockSaleDto);
    }

    @PostMapping("/addSale")
    public Mono<SaleResponseDto> addSale(@RequestBody AddSaleRequest requestDto) {
        return null;
    }
}
