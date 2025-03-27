package ru.unisafe.psemployee.service;

import reactor.core.publisher.Flux;
import ru.unisafe.psemployee.model.WebRequest;

public interface WebRequestService {
    Flux<WebRequest> getReceivingList(String searchParam);
}
