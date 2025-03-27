package ru.unisafe.psemployee.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.unisafe.psemployee.service.WebRequestSearchService;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class WebRequestSearchServiceImpl implements WebRequestSearchService {

    @Override
    public String buildReceivingListQuery(Map<String, Object> params) {
        String baseQuery = """
            SELECT * FROM web_requests
            WHERE is_completed = true
              AND (direction_type = 'Вернуть на склад' OR direction_type = 'Реверсная заявка')
              AND is_received = false
        """;

        StringBuilder query = new StringBuilder(baseQuery);

        if (params.containsKey("sdekId")) {
            query.append(" AND sdek_id = :sdekId");
        } else if (params.containsKey("id")) {
            query.append(" AND id = :id");
        } else if (params.containsKey("login")) {
            query.append(" AND login = :login");
        } else if (params.containsKey("stationCode")) {
            query.append(" AND station_code = :stationCode");
        } else if (params.containsKey("address")) {
            query.append(" AND address ILIKE :address");
        }

        return query.toString();
    }

    @Override
    public Map<String, Object> buildParams(String searchParam) {
        Map<String, Object> params = new HashMap<>();
        if (searchParam.matches("\\d+")) {
            if (searchParam.length() > 9) {
                params.put("sdekId", searchParam);
            } else {
                params.put("id", Integer.parseInt(searchParam));
            }
        } else if (searchParam.toLowerCase().startsWith("station")) {
            params.put("login", searchParam);
        } else if (searchParam.length() < 8) {
            params.put("stationCode", searchParam);
        } else {
            params.put("address", "%" + searchParam + "%");
        }
        return params;
    }
}
