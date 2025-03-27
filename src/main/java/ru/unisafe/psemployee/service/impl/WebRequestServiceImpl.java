package ru.unisafe.psemployee.service.impl;

import io.r2dbc.spi.Row;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.unisafe.psemployee.dto.request.WebRequestReceiveRequest;
import ru.unisafe.psemployee.dto.response.BaseResponse;
import ru.unisafe.psemployee.model.WebRequest;
import ru.unisafe.psemployee.repository.r2dbc.StoreItemsRepository;
import ru.unisafe.psemployee.repository.r2dbc.WebItemRepository;
import ru.unisafe.psemployee.repository.r2dbc.WebRequestRepository;
import ru.unisafe.psemployee.service.WebRequestService;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Service
public class WebRequestServiceImpl implements WebRequestService {

    private final DatabaseClient databaseClient;
    private final WebRequestRepository webRequestRepository;
    private final WebItemRepository webItemRepository;
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
    public Mono<BaseResponse> receiveRequest(WebRequestReceiveRequest request) {
        long requestId = request.getRequestId();

        return webRequestRepository.markAsReceived(requestId)
                .then(updateMainStore(requestId))
                .thenReturn(new BaseResponse(true, "Заявка успешно получена"))
                .onErrorResume(err -> {
                    log.error("Ошибка при обработке заявки", err);
                    return Mono.just(new BaseResponse(false, "Ошибка при обновлении данных"));
                });
    }

    private Mono<Void> updateMainStore(long requestId) {
        return webItemRepository.findItemsByRequestId(requestId)
                .flatMap(item -> storeItemsRepository
                        .updateItemValue(item.getItemId(), item.getItemValue()))
                .then();
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
