package ru.unisafe.psemployee.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import ru.unisafe.psemployee.dto.TableSaleInfo;
import ru.unisafe.psemployee.dto.response.BlockSaleResponseDto;
import ru.unisafe.psemployee.dto.response.MessageDto;

import java.util.List;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;

@Slf4j
@RequiredArgsConstructor
@Repository
public class SalesRepository {

    private final DSLContext dsl;

    public Mono<BlockSaleResponseDto> blockSale(int id, boolean isBlock, TableSaleInfo tableInfo) {
        log.info("Блокируем или разблокируем: {}", isBlock);

        return Mono.from(
                        dsl.update(table(tableInfo.tableName()))
                                .set(field("is_blocked"), isBlock)
                                .set(field("was_loaded"), true)
                                .where(field("id").eq(id))
                )
                .flatMap(updatedRows -> {
                    if (updatedRows == 0) {
                        return Mono.just(new BlockSaleResponseDto(false, List.of(new MessageDto("Продажа не найдена."))));
                    }

                    String msg = isBlock ? "Продажа зарегистрирована." : "Продажа отменена.";
                    log.info("Результат операции: {}", msg);
                    return Mono.just(new BlockSaleResponseDto(true, List.of(new MessageDto(msg))));
                });
    }

}
