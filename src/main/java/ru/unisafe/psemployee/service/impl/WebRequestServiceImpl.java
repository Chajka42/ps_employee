package ru.unisafe.psemployee.service.impl;

import io.r2dbc.spi.Row;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.unisafe.psemployee.dto.request.WebRequestReceiveRequest;
import ru.unisafe.psemployee.dto.response.BaseResponse;
import ru.unisafe.psemployee.model.WebRequest;
import ru.unisafe.psemployee.repository.r2dbc.StoreItemsRepository;
import ru.unisafe.psemployee.repository.r2dbc.WebItemsRepository;
import ru.unisafe.psemployee.repository.r2dbc.WebRequestRepository;
import ru.unisafe.psemployee.service.WebRequestService;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Service
public class WebRequestServiceImpl implements WebRequestService {

    private final DatabaseClient databaseClient;
    private final WebRequestRepository webRequestRepository;
    private final WebItemsRepository webItemsRepository;
    private final StoreItemsRepository storeItemsRepository;

    @Override
    public Flux<WebRequest> getReceivingList(String searchParam) {
        String query = """
                SELECT * FROM web_requests
                WHERE is_completed = true
                  AND (direction_type = 'Вернуть на склад' OR direction_type = 'Реверсная заявка')
                  AND is_received = false
                  AND (
                       CAST(id AS CHAR) LIKE :search
                    OR LOWER(login) LIKE LOWER(:search)
                    OR LOWER(station_code) LIKE LOWER(:search)
                    OR LOWER(visor_name) LIKE LOWER(:search)
                    OR LOWER(address) LIKE LOWER(:search)
                    OR CAST(sdek_id AS CHAR) LIKE :search
                    OR LOWER(comment) LIKE LOWER(:search)
                    OR LOWER(partner_name) LIKE LOWER(:search)
                  )
                """;

        return databaseClient.sql(query)
                .bind("search", "%" + searchParam.toLowerCase() + "%")
                .map((row, metadata) -> mapRowToWebRequest(row))
                .all();
    }

    @Override
    public Mono<ResponseEntity<BaseResponse>> receiveRequest(WebRequestReceiveRequest request) {
        long requestId = request.getRequestId();

        return webRequestRepository.markAsReceived(requestId)
                .flatMap(updatedRows -> {
                    if (updatedRows == 0) {
                        return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body(new BaseResponse(false, "Заявка с таким requestId не найдена")));
                    }
                    return updateMainStore(requestId)
                            .thenReturn(ResponseEntity.ok(new BaseResponse(true, "Заявка успешно получена")));
                })
                .onErrorResume(err -> {
                    log.error("Ошибка при обработке заявки", err);
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(new BaseResponse(false, "Ошибка при обновлении данных")));
                });
    }

    private Mono<Void> updateMainStore(long requestId) {
        return webItemsRepository.findItemsByRequestId(requestId)
                .flatMap(item -> storeItemsRepository
                        .updateItemValue(item.getItemId(), item.getItemValue()))
                .then();
    }

    @Override
    public Mono<ResponseEntity<BaseResponse>> deleteRequest(WebRequestReceiveRequest request) {
        long requestId = request.getRequestId();

        return webRequestRepository.existsById(requestId)
                .flatMap(exists -> {
                    if (!exists) {
                        return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body(new BaseResponse(false, "Реквест с таким requestId не найден")));
                    }
                    return webRequestRepository.deleteById(requestId)
                            .then(Mono.just(ResponseEntity
                                    .status(HttpStatus.OK)
                                    .body(new BaseResponse(true, "Реквест успешно удален"))));
                })
                .onErrorResume(err -> {
                    log.error("Ошибка при удалении реквеста: {}", err.getMessage(), err);
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(new BaseResponse(false, "Ошибка сервера")));
                });
    }

    private WebRequest mapRowToWebRequest(Row row) {
        return new WebRequest(
                row.get("id", Integer.class),
                row.get("login", String.class),
                row.get("station_code", String.class),
                row.get("visor_id", Integer.class),
                row.get("visor_name", String.class),
                row.get("address", String.class),
                row.get("created", LocalDateTime.class),
                row.get("sdek_id", String.class),
                row.get("completed", LocalDateTime.class),
                row.get("is_completed", Boolean.class),
                row.get("direction_type", String.class),
                row.get("agregator_type", String.class),
                row.get("comment", String.class),
                row.get("partner_id", Integer.class),
                row.get("partner_name", String.class),
                row.get("is_received", Boolean.class),
                row.get("is_boxed", Boolean.class),
                row.get("was_edited", LocalDateTime.class),
                row.get("see", Boolean.class),
                row.get("was_received", LocalDateTime.class),
                row.get("store_photo", String.class),
                row.get("boxed_person", String.class)
        );
    }
}
