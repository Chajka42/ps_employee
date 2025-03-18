package ru.unisafe.psemployee.service.impl;

import io.netty.handler.codec.http.HttpResponseStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.unisafe.psemployee.dto.TableSaleInfo;
import ru.unisafe.psemployee.dto.request.AddSaleRequest;
import ru.unisafe.psemployee.dto.request.BlockSaleDto;
import ru.unisafe.psemployee.dto.request.SaleRequestDto;
import ru.unisafe.psemployee.dto.response.BlockSaleResponseDto;
import ru.unisafe.psemployee.dto.response.SaleDto;
import ru.unisafe.psemployee.dto.response.SaleResponseDto;
import ru.unisafe.psemployee.enums.PartnerEnum;
import ru.unisafe.psemployee.model.Partner;
import ru.unisafe.psemployee.repository.PartnerRepositoryJOOQ;
import ru.unisafe.psemployee.repository.SalesRepository;
import ru.unisafe.psemployee.repository.StationRepositoryJOOQ;
import ru.unisafe.psemployee.repository.r2dbc.PartnerRepository;
import ru.unisafe.psemployee.service.EmployeeSaleHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.jooq.impl.DSL.field;

@RequiredArgsConstructor
@Slf4j
@Service
public class EmployeeSaleHandlerImpl implements EmployeeSaleHandler {

    private final PartnerRepositoryJOOQ partnerJooq;
    private final PartnerRepository partnerRepository;
    private final SalesRepository salesRepository;
    private final StationRepositoryJOOQ stationJooq;
    private final DSLContext dslContext;

    @Override
    public Mono<SaleResponseDto> getSaleJson(SaleRequestDto requestDto) {
        String login = requestDto.getLogin();
        log.info("Login: {}", login);
        return partnerJooq.getPartnerIdByStation(login)
                .flatMap(partnerId -> {
                    log.info("PartnerID: {}", partnerId);
                    PartnerEnum partnerEnum = PartnerEnum.fromId(partnerId);
                    return partnerRepository.findById(partnerId)
                            .map(Partner::getPartnerName)
                            .flatMap(partnerName -> {
                                TableSaleInfo tableSaleInfo = getTableAndColumnName(partnerEnum);
                                return stationJooq.getStationCodeByLogin(login)
                                        .flatMap(stationCode -> getSalesFromDatabase(tableSaleInfo, login)
                                                .map(sales -> mapToSaleDtoResponse(sales, login, stationCode, partnerId, partnerName))
                                        );
                            });
                })
                .onErrorResume(e -> {
                    log.error("Ошибка при получении данных: ", e);
                    return Mono.just(new SaleResponseDto(false, login, null, null, null, List.of(), List.of()));
                });
    }

    private SaleResponseDto mapToSaleDtoResponse(List<SaleDto> sales, String login, String stationCode, Integer partnerId, String partnerName) {
        List<SaleDto> activeSales = new ArrayList<>();
        List<SaleDto> blockedSales = new ArrayList<>();

        sales.forEach(sale -> {
            if ("active".equals(sale.getStatus())) {
                activeSales.add(sale);
            } else {
                blockedSales.add(sale);
            }
        });

        return new SaleResponseDto(true, login, stationCode, partnerId, partnerName, activeSales, blockedSales);
    }

    private Mono<List<SaleDto>> getSalesFromDatabase(TableSaleInfo tableSaleInfo, String login) {
        return Mono.fromCallable(() -> {
                    var result = dslContext.select(
                                    field("id"),
                                    field("date"),
                                    field("type"),
                                    field("model"),
                                    field("was_loaded"),
                                    field(tableSaleInfo.columnName()).as("status")
                            )
                            .from(tableSaleInfo.tableName())
                            .where(field("stationLogin").eq(login))
                            .and(field("date").ge(LocalDate.now().minusDays(1)))
                            .fetch();

                    return result.stream().map(sale -> new SaleDto(
                            sale.get("id", Integer.class),
                            sale.get("was_loaded", Boolean.class),
                            sale.get("model", Integer.class),
                            mapSaleType(sale.get("type", Integer.class)),
                            sale.get("date", LocalDate.class).toString(),
                            sale.get("status", Boolean.class) ? "blocked" : "active"
                    )).toList();
                })
                .onErrorResume(e -> {
                    log.error("Ошибка при запросе продаж: ", e);
                    return Mono.just(List.of());
                });
    }

    private String mapSaleType(Integer type) {
        if (type == null) {
            return "неизвестно";
        }
        return switch (type) {
            case 1 -> "брак";
            case 2 -> "обучение";
            case 3 -> "отладка";
            case 4 -> "гарантия";
            default -> "продажа";
        };
    }

    @Override
    public Mono<BlockSaleResponseDto> blockSale(BlockSaleDto blockSaleDto) {
        log.info("Block sale. partnerId: {}, id: {}", blockSaleDto.getPartnerId(), blockSaleDto.getId());
        TableSaleInfo tableInfo = getTableAndColumnName(PartnerEnum.fromId(blockSaleDto.getPartnerId()));
        return salesRepository.blockSale(blockSaleDto.getId(), true, tableInfo);
    }

    private TableSaleInfo getTableAndColumnName(PartnerEnum partner) {
        String table_name;
        String column_name;

        switch (partner) {
            case MTS -> {
                table_name = "mts_sales";
                column_name = "is_blocked";
            }
            case MEGAFON -> {
                table_name = "megafon_sales";
                column_name = "activated";
            }
            case EVRIKA -> {
                table_name = "evrika_sales";
                column_name = "is_blocked";
            }
            case MVIDEO -> {
                table_name = "mvideo_sales";
                column_name = "is_blocked";
            }
            case SVYAZ_ON -> {
                table_name = "svyazon_sales";
                column_name = "is_blocked";
            }
            default -> {
                table_name = "other_sales";
                column_name = "is_blocked";
            }
        }

        return new TableSaleInfo(table_name, column_name);
    }

    @Override
    public Mono<SaleResponseDto> addSale(AddSaleRequest requestDto) {
        return createReceiptEmployee(
                requestDto.getLogin(),
                requestDto.getCode(),
                requestDto.getType(),
                requestDto.getModel(),
                requestDto.getPartnerId());
    }

    private Mono<SaleResponseDto> createReceiptEmployee(String login, String code, int type, int model, int partnerId) {
        return null;
    }

}
