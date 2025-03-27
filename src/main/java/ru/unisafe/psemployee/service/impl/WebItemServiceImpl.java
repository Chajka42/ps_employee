package ru.unisafe.psemployee.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.unisafe.psemployee.dto.request.AddWebItemRequest;
import ru.unisafe.psemployee.dto.response.BaseResponse;
import ru.unisafe.psemployee.model.WebItem;
import ru.unisafe.psemployee.repository.r2dbc.WebItemsRepository;
import ru.unisafe.psemployee.repository.r2dbc.WebRequestRepository;
import ru.unisafe.psemployee.service.WebItemService;

@RequiredArgsConstructor
@Slf4j
@Service
public class WebItemServiceImpl implements WebItemService {

    private final WebItemsRepository webItemsRepository;
    private final WebRequestRepository webRequestRepository;

    @Override
    public Mono<BaseResponse> addRequestItem(AddWebItemRequest request) {
        return webRequestRepository.existsById(request.getRequestId())
                .flatMap(exists -> {
                    if (!exists) {
                        return Mono.just(new BaseResponse(false, "Заявка с таким requestId не найдена"));
                    }

                    WebItem webItem = new WebItem();
                    webItem.setRequestId(request.getRequestId());
                    webItem.setToTt(request.getIsToTt());
                    webItem.setItemId(request.getItemId());
                    webItem.setItemName(request.getItemName());
                    webItem.setItemTeg(request.getItemTeg());
                    webItem.setLogin(request.getLogin());
                    webItem.setItemValue(request.getItemValue());

                    return webItemsRepository.save(webItem)
                            .then(webRequestRepository.updateWasEdited(request.getRequestId()))
                            .thenReturn(new BaseResponse(true, "Предмет успешно добавлен"));
                })
                .onErrorResume(err -> {
                    log.error("Ошибка при добавлении предмета", err);
                    return Mono.just(new BaseResponse(false, "Ошибка при добавлении предмета"));
                });
    }
}
