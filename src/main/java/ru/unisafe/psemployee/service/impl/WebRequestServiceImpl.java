package ru.unisafe.psemployee.service.impl;

import io.r2dbc.spi.Row;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import ru.unisafe.psemployee.model.WebRequest;
import ru.unisafe.psemployee.service.WebRequestSearchService;
import ru.unisafe.psemployee.service.WebRequestService;

import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class WebRequestServiceImpl implements WebRequestService {

    private final DatabaseClient databaseClient;
    private final WebRequestSearchService searchService;

    @Override
    public Flux<WebRequest> getReceivingList(String searchParam) {
        Map<String, Object> params = searchService.buildParams(searchParam);
        String query = searchService.buildReceivingListQuery(params);

        DatabaseClient.GenericExecuteSpec spec = databaseClient.sql(query);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            spec = spec.bind(entry.getKey(), entry.getValue());
        }

        return spec.map((row, metadata) -> mapRowToWebRequest(row)).all();
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
