package ru.unisafe.psemployee.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.unisafe.psemployee.dto.TableSaleInfo;
import ru.unisafe.psemployee.enums.PartnerId;
import ru.unisafe.psemployee.repository.PartnerRepositoryJOOQ;
import ru.unisafe.psemployee.repository.StationRepositoryJOOQ;
import ru.unisafe.psemployee.service.EmployeeSaleHandler;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.jooq.impl.DSL.field;

@RequiredArgsConstructor
@Slf4j
@Service
public class EmployeeSaleHandlerImpl implements EmployeeSaleHandler {

    private final PartnerRepositoryJOOQ partnerJooq;
    private final StationRepositoryJOOQ stationJooq;
    private final DSLContext dslContext;

    @Override
    public Mono<String> getSaleJson(String login) {

        log.info("Login: {}", login);
        return partnerJooq.getPartnerIdByStation(login)
                .flatMap(partnerId -> {
                    log.info("PartnerID: {}", partnerId);
                    PartnerId partnerEnum = PartnerId.fromId(partnerId);
                    TableSaleInfo tableSaleInfo = getTableAndColumnName(partnerEnum);
                    return stationJooq.getStationCodeByLogin(login)
                            .flatMap(stationCode -> getSalesFromDatabase(tableSaleInfo, login)
                                    .map(salesJson -> {
                                        ObjectNode responseNode = JsonNodeFactory.instance.objectNode();
                                        responseNode.put("success", true);
                                        responseNode.put("login", login);
                                        responseNode.put("code", stationCode);
                                        responseNode.put("partner_id", partnerId);
                                        responseNode.put("partner", partnerEnum.name());
                                        responseNode.set("salesData", salesJson);
                                        return responseNode.toString();
                                    }));
                })
                .onErrorResume(e -> {
                    log.error("Ошибка при получении данных: ", e);
                    return Mono.just("{\"success\":false, \"error\":\"Ошибка при получении данных\"}");
                });
    }

    private Mono<JsonNode> getSalesFromDatabase(TableSaleInfo tableSaleInfo, String login) {
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

                    ArrayNode salesList = JsonNodeFactory.instance.arrayNode();

                    for (var sale : result) {
                        ObjectNode saleNode = JsonNodeFactory.instance.objectNode();
                        saleNode.put("id", sale.get("id", Integer.class));
                        saleNode.put("was_loaded", sale.get("was_loaded", Boolean.class));
                        saleNode.put("model", sale.get("model", Integer.class));
                        saleNode.put("type", mapSaleType(sale.get("type", Integer.class)));
                        saleNode.put("time", sale.get("date", LocalDate.class).toString());

                        boolean isBlocked = sale.get("status", Boolean.class);
                        saleNode.put("status", isBlocked ? "blocked" : "active");

                        salesList.add(saleNode);
                    }

                    return (JsonNode) salesList;
                })
                .onErrorResume(e -> {
                    log.error("Ошибка при запросе продаж: ", e);
                    return Mono.just((JsonNode) JsonNodeFactory.instance.arrayNode());
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

    private TableSaleInfo getTableAndColumnName(PartnerId partnerIdEnum) {
        String table_name;
        String column_name;

        switch (partnerIdEnum) {
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
            case SVYAZON -> {
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
}
