package ru.unisafe.psemployee.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;


class OpenStreetMapServiceTest {

    private final OpenStreetMapServiceImpl openStreetMapService =
            new OpenStreetMapServiceImpl(WebClient.builder());

    @Test
    void testGetAddressFromCoordinates() {
        //Филевский бульвар дом 6
        double latitude = 55.76501181442035;
        double longitude = 37.48914487704136;

        StepVerifier.create(openStreetMapService.getAddressFromCoordinates(latitude, longitude))
                .assertNext(address -> {
                    System.out.println("Ответ от API: " + address);
                    Assertions.assertNotNull(address, "Адрес не должен быть null");
                    Assertions.assertFalse(address.isEmpty(), "Адрес не должен быть пустым");
                    Assertions.assertTrue(address.contains("Филёвский бульвар"), "Адрес должен содержать Филевский бульвар");
                })
                .verifyComplete();
    }

}
