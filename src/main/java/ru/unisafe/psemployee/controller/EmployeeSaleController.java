package ru.unisafe.psemployee.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ru.unisafe.psemployee.dto.response.BlockSaleResponseDto;
import ru.unisafe.psemployee.dto.response.SaleDtoResponse;
import ru.unisafe.psemployee.service.EmployeeSaleHandler;

import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/employee/api")
@RestController
public class EmployeeSaleController {

    private final EmployeeSaleHandler employeeSaleHandler;

    @GetMapping("/getSaleJson")
    public Mono<SaleDtoResponse> getSaleJson(@RequestParam String login) {
        return employeeSaleHandler.getSaleJson(login);
    }

    @GetMapping("/blockSale")
    public Mono<BlockSaleResponseDto> blockSale(@RequestParam("partner_id") Integer partnerId, @RequestParam Integer id) {
        return employeeSaleHandler.blockSale(partnerId, id);
    }
}
