package ru.unisafe.psemployee.service;

import java.util.Map;

public interface WebRequestSearchService {
    String buildReceivingListQuery(Map<String, Object> params);

    Map<String, Object> buildParams(String searchParam);
}
