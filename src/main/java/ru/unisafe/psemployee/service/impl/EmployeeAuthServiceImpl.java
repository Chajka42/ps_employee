package ru.unisafe.psemployee.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.tools.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.unisafe.psemployee.dto.response.BaseResponse;
import ru.unisafe.psemployee.dto.response.CheckTokenResponse;
import ru.unisafe.psemployee.dto.response.PasswordCheckResponse;
import ru.unisafe.psemployee.repository.EmployeeRepositoryJOOQ;
import ru.unisafe.psemployee.service.EmployeeAuthService;
import ru.unisafe.psemployee.service.MD5;
import ru.unisafe.psemployee.service.OpenStreetMapService;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class EmployeeAuthServiceImpl implements EmployeeAuthService {

    private final EmployeeRepositoryJOOQ employeeRepository;
    private final OpenStreetMapService geoService;
    private final MD5 md5;

    @Value("${ps.app.version}")
    private int webAppVersion;

    @Override
    public Mono<BaseResponse> checkPassword(Map<String, List<String>> queryParams) {

        String password = queryParams.getOrDefault("password", List.of("")).get(0);
        log.info("Pass from query param {}", password);

        if (!queryParams.containsKey("app_version") || queryParams.get("app_version").isEmpty()) {
            return Mono.just(new BaseResponse(false, "Обнови страницу или веб приложение."));
        }

        int appVersion = Integer.parseInt(queryParams.get("app_version").get(0));

        if (appVersion < webAppVersion) {
            return Mono.just(new BaseResponse(false, "Обнови страницу или веб приложение."));
        }

        try {
            String passFunction = md5.calculateMD5(password);
            log.info("Calculated pass: {}", passFunction);

            return employeeRepository.findEmployeeByPassFunction(passFunction)
                    .flatMap(optionalEmployee -> optionalEmployee
                            .map(employee -> Mono.just((BaseResponse) new PasswordCheckResponse(true, "Пароль верный", employee)))
                            .orElseGet(() -> Mono.just(new BaseResponse(false, "Неправильный пароль")))
                    );

        } catch (NoSuchAlgorithmException e) {
            return Mono.just(new BaseResponse(false, "Неправильный пароль"));
        }

    }

    @Override
    public Mono<BaseResponse> checkToken(Map<String, List<String>> queryParams) {
        String lat = queryParams.getOrDefault("lat", List.of("")).get(0);
        String lon = queryParams.getOrDefault("lon", List.of("")).get(0);

        if (lat.isEmpty() || lon.isEmpty() || lat.length() < 5 || lon.length() < 5) {
            return Mono.just(new BaseResponse(false, "Требуется доступ к ГЕО данным."));
        }

        String token = queryParams.getOrDefault("relict_token", List.of("")).get(0);

        return employeeRepository.checkAuthEmployee(token)
                .flatMap(auth -> {
                    if (auth) {
                        return processLocationUpdate(token, lat, lon);
                    } else {
                        return employeeRepository.checkRelictAuthEmployee(token)
                                .flatMap(relictAuth -> {
                                    if (relictAuth) {
                                        return tokenGet(token)
                                                .flatMap(newToken -> processLocationUpdate(newToken, lat, lon)
                                                        .map(response -> new CheckTokenResponse(true, "Токен валиден", newToken))
                                                );
                                    } else {
                                        return Mono.just(new BaseResponse(false, "Твоя сессия устарела. Пожалуйста, авторизуйся заново."));
                                    }
                                });
                    }
                });
    }

    private Mono<CheckTokenResponse> processLocationUpdate(String token, String lat, String lon) {
        return employeeRepository.getEmployeeOriginId(token)
                .flatMap(id -> {
                    double latitude = Double.parseDouble(lat);
                    double longitude = Double.parseDouble(lon);
                    return geoService.getAddressFromCoordinates(latitude, longitude)
                            .flatMap(address -> employeeRepository.updateEmployeeLocation(id, latitude, longitude, address)
                                    .thenReturn(new CheckTokenResponse(true, "Токен получен из бд", token))
                            );
                })
                .onErrorResume(e -> Mono.just(new CheckTokenResponse(false, "Внутренняя ошибка сервера.", token)));
    }

    public Mono<String> tokenGet(String relictToken) {
        return employeeRepository.getTokenByRelictToken(relictToken)
                .switchIfEmpty(Mono.defer(() -> {
                    JSONObject json = new JSONObject();
                    json.put("success", false);
                    json.put("msg", "Токен не найден");
                    return Mono.just(json.toString());
                }));
    }

}
