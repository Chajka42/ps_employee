package ru.unisafe.psemployee.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.unisafe.psemployee.service.OpenStreetMapService;

@Service
public class OpenStreetMapServiceImpl implements OpenStreetMapService {

    private static final String NOMINATIM_ENDPOINT = "https://nominatim.openstreetmap.org/reverse";
    private final WebClient webClient;

    public OpenStreetMapServiceImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(NOMINATIM_ENDPOINT).build();
    }

    @Override
    public Mono<String> getAddressFromCoordinates(double latitude, double longitude) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("format", "json")
                        .queryParam("lat", latitude)
                        .queryParam("lon", longitude)
                        .build())
                .retrieve()
                .bodyToMono(JsonNode.class)
                .map(json -> json.has("display_name") ? json.get("display_name").asText() : latitude + " " + longitude)
                .map(displayName -> displayName.replaceAll("\\b[A-ZА-Я]+\\b", "(!)"))
                .onErrorReturn(latitude + " " + longitude);
    }
}
