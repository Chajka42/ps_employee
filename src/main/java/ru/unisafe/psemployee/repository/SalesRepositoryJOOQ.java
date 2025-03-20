package ru.unisafe.psemployee.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import ru.unisafe.psemployee.dto.SaleToSave;
import ru.unisafe.psemployee.dto.TableSaleInfo;
import ru.unisafe.psemployee.dto.request.MegafonTariffRequest;
import ru.unisafe.psemployee.dto.request.MegafonTariffUpdatePhoneRequest;
import ru.unisafe.psemployee.dto.response.*;

import java.time.LocalDateTime;
import java.util.List;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;

@Slf4j
@RequiredArgsConstructor
@Repository
public class SalesRepositoryJOOQ {

    private final DSLContext dsl;

    public Mono<BlockSaleResponseDto> blockSale(int id, boolean isBlock, TableSaleInfo tableInfo) {
        log.info("Блокируем или разблокируем: {}", isBlock);

        return Mono.from(
                        dsl.update(table(tableInfo.tableName()))
                                .set(field(tableInfo.columnName()), isBlock)
                                .set(field("was_loaded"), true)
                                .where(field("id").eq(id))
                )
                .flatMap(updatedRows -> {
                    if (updatedRows == 0) {
                        log.warn("Продажа не была найдена. Table: {}", tableInfo.tableName());
                        return Mono.just(new BlockSaleResponseDto(false, List.of(new MessageDto("Продажа не найдена."))));
                    }

                    String msg = isBlock ? "Продажа зарегистрирована." : "Продажа отменена.";
                    log.info("Результат операции: {}", msg);
                    return Mono.just(new BlockSaleResponseDto(true, List.of(new MessageDto(msg))));
                });
    }

    public Mono<Long> addSale(TableSaleInfo tableInfo, SaleToSave sale) {
        return Mono.from(
                        dsl.insertInto(table(tableInfo.tableName()))
                                .columns(
                                        field("stationCode"),
                                        field("stationLogin"),
                                        field("date"),
                                        field("server_time"),
                                        field("article"),
                                        field("receipt"),
                                        field("who"),
                                        field("type"),
                                        field("model")
                                )
                                .values(
                                        sale.stationCode(),
                                        sale.stationLogin(),
                                        sale.date(),
                                        sale.serverTime(),
                                        sale.article(),
                                        sale.receipt(),
                                        sale.who(),
                                        sale.type(),
                                        sale.model()
                                )
                                .returningResult(field("id"))
                ).map(record -> record.get(field("id", Long.class)))
                .defaultIfEmpty(0L);
    }

    public Mono<MegafonTariffResponse> getMegafonTariffSale(MegafonTariffRequest megafonTariffRequest) {
        return Mono.from(
                dsl.select(
                                field("id", Integer.class),
                                field("receipt", String.class),
                                field("server_time", LocalDateTime.class),
                                field("phone", String.class)
                        )
                        .from(table("megafon_sales"))
                        .where(field("receipt").like("%" + megafonTariffRequest.getSaleKey() + "%")
                                .and(field("article").in("900140490", "900140491", "900139119", "900139120")))
                        .orderBy(field("id").desc())
                        .limit(1)
        ).map(record -> new MegafonTariffResponse(
                true,
                record.get("id", Integer.class),
                record.get("receipt", String.class),
                record.get("server_time", LocalDateTime.class),
                record.get("phone", String.class)
        )).defaultIfEmpty(new MegafonTariffResponse(false, 0, null, null, null));
    }

    public Mono<BaseResponse> updatePhoneInMegafonTariffSale(MegafonTariffUpdatePhoneRequest request) {
        return Mono.from(dsl
                .update(table("megafon_sales"))
                .set(field("phone", String.class), request.getNewPhone())
                .where(field("id", Integer.class).eq(request.getId())))
                .map(updatedRows -> {
                    boolean success = updatedRows > 0;
                    String message = success ? "Телефон успешно обновлен" : "Произошла ошибка в процессе обновления телефона";
                    return new BaseResponse(success, message);
                });
    }

}
