package ru.unisafe.psemployee.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.unisafe.psemployee.dto.*;
import ru.unisafe.psemployee.dto.request.AddWebItemRequest;
import ru.unisafe.psemployee.dto.request.GetRequestInfoRequest;
import ru.unisafe.psemployee.dto.response.BaseResponse;
import ru.unisafe.psemployee.dto.response.RequestInfoResponse;
import ru.unisafe.psemployee.model.WebItem;
import ru.unisafe.psemployee.repository.r2dbc.StoreItemsRepository;
import ru.unisafe.psemployee.repository.r2dbc.WebItemsRepository;
import ru.unisafe.psemployee.repository.r2dbc.WebRequestRepository;
import ru.unisafe.psemployee.service.WebItemService;

@RequiredArgsConstructor
@Slf4j
@Service
public class WebItemServiceImpl implements WebItemService {

    private final WebItemsRepository webItemsRepository;
    private final WebRequestRepository webRequestRepository;
    private final StoreItemsRepository storeItemRepository;

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

    @Override
    public Mono<RequestInfoResponse> getRequestInfo(GetRequestInfoRequest request) {
        long requestId = request.getRequestId();
        boolean see = request.isSee();

        Mono<Void> updateMono = see
                ? webRequestRepository.updateWebRequestSetSeeTrue(requestId).then()
                : Mono.empty();

        return updateMono.then(
                webRequestRepository.findById(requestId)
                        .flatMap(requestEntity -> {
                            String login = requestEntity.getLogin();
                            long partnerId = requestEntity.getPartnerId();

                            Flux<WebRequestDto> fluxWebRequests = webRequestRepository.getWebRequestPlusTtsInfo(requestId);
                            Flux<CategoryDto> fluxCategories = storeItemRepository.selectFromStoreItemsAndStoreCategories(partnerId);

                            Flux<WebItemDto> fluxWebItemsTo = webItemsRepository.findAllByToTtAndRequestId(true, requestId)
                                    .map(this::mapToDto);

                            Flux<WebItemDto> fluxWebItemsFrom = webItemsRepository.findAllByToTtAndRequestId(false, requestId)
                                    .map(this::mapToDto);

                            Flux<OtherRequestDto> fluxOtherRequests = webRequestRepository.selectOtherRequests(requestId, login);
                            Flux<ItemRequestBeforeDto> fluxItemRequestedBefore = webRequestRepository.selectItemsRequestedBefore(login);

                            return Mono.zip(
                                    fluxWebRequests.collectList(),
                                    fluxCategories.collectList(),
                                    fluxWebItemsTo.collectList(),
                                    fluxWebItemsFrom.collectList(),
                                    fluxOtherRequests.collectList(),
                                    fluxItemRequestedBefore.collectList()
                            ).map(tuple -> {
                                RequestInfoResponse response = new RequestInfoResponse();
                                response.setSuccess(true);
                                response.setRequest(tuple.getT1());
                                response.setCategories(tuple.getT2());
                                response.setItemsTo(tuple.getT3());
                                response.setItemsFrom(tuple.getT4());
                                response.setOtherRequests(tuple.getT5());
                                response.setRequestedBefore(tuple.getT6());
                                return response;
                            });
                        })
        );
    }

    private WebItemDto mapToDto(WebItem webItem) {
        WebItemDto dto = new WebItemDto();
        dto.setId(webItem.getId());
        dto.setToTt(webItem.isToTt());
        dto.setItemId(webItem.getItemId());
        dto.setItemName(webItem.getItemName());
        dto.setItemTeg(webItem.getItemTeg());
        dto.setItemValue(webItem.getItemValue());
        return dto;
    }
}
