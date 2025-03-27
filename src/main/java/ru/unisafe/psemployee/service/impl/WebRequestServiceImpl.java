package ru.unisafe.psemployee.service.impl;

import io.r2dbc.spi.Row;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import ru.unisafe.psemployee.model.WebRequest;
import ru.unisafe.psemployee.service.WebRequestService;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Service
public class WebRequestServiceImpl implements WebRequestService {

    private final DatabaseClient databaseClient;

    @Override
    public Flux<WebRequest> getReceivingList(String searchParam) {
        String baseQuery = """
            SELECT * FROM web_requests
            WHERE is_completed = true
              AND (direction_type = 'Вернуть на склад' OR direction_type = 'Реверсная заявка')
              AND is_received = false
        """;

        String query = buildDynamicQuery(baseQuery, searchParam);
        log.info("Executing query: {}", query);

        return databaseClient.sql(query)
                .map((row, metadata) -> mapRowToWebRequest(row))
                .all();
    }

    private String buildDynamicQuery(String baseQuery, String searchParam) {
        if (searchParam == null || searchParam.isBlank()) {
            return baseQuery;
        }

        StringBuilder query = new StringBuilder(baseQuery);

        if (searchParam.matches("\\d+")) {
            if (searchParam.length() > 9) {
                query.append(" AND sdek_id = '").append(searchParam).append("'");
            } else {
                query.append(" AND id = '").append(searchParam).append("'");
            }
        } else {
            if (searchParam.toLowerCase().startsWith("station")) {
                query.append(" AND login = '").append(searchParam).append("'");
            } else if (searchParam.length() < 8) {
                query.append(" AND station_code = '").append(searchParam).append("'");
            } else {
                query.append(" AND address ILIKE '%").append(searchParam).append("%'");
            }
        }

        return query.toString();
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
