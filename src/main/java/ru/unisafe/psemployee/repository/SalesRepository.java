package ru.unisafe.psemployee.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import ru.unisafe.psemployee.dto.SaleToSave;
import ru.unisafe.psemployee.dto.TableSaleInfo;
import ru.unisafe.psemployee.dto.request.AddSaleRequest;
import ru.unisafe.psemployee.dto.response.BlockSaleResponseDto;
import ru.unisafe.psemployee.dto.response.MessageDto;
import ru.unisafe.psemployee.dto.response.SaleResponseDto;

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

    //DatabaseService databaseService = new DatabaseService(Constants.URL_DB, Constants.USER_DB, Constants.PASSWORD_DB);
    //            PreparedStatement statement;
    //            Connection connection;
    //            try {
    //                connection = databaseService.getConnection();
    //                statement = connection.prepareStatement("INSERT INTO " + table_name + " (stationCode, stationLogin, " + column_name + ", article, date, server_time, who, type, model) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
    //                statement.setString(1, stationCode);
    //                statement.setString(2, login);
    //                statement.setString(3, receipt);
    //                statement.setString(4, article);
    //                statement.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
    //                statement.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
    //                statement.setInt(7, 0);
    //                statement.setInt(8, type);
    //                statement.setInt(9, model);
    //                statement.executeUpdate();
    //                databaseService.closeConnection(connection);
    //            } catch (SQLException e) {
    //                throw new RuntimeException(e);
    //            }

}
