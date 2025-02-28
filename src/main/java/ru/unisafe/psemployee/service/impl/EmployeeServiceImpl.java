package ru.unisafe.psemployee.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.tools.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.unisafe.psemployee.dto.Employee;
import ru.unisafe.psemployee.repository.EmployeeRepository;
import ru.unisafe.psemployee.service.EmployeeService;
import ru.unisafe.psemployee.service.MD5;
import ru.unisafe.psemployee.service.OpenStreetMapService;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final OpenStreetMapService geoService;
    private final MD5 md5;

    @Value("${ps.app.version}")
    private int webAppVersion;

    @Override
    public Flux<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public Mono<Employee> getEmployeeById(int id) {
        return employeeRepository.findById(id);
    }

    @Override
    public Mono<String> checkPassword(Map<String, List<String>> queryParams) {
        String password = queryParams.get("password").get(0);
        log.info("Pass from query param {}", password);
        JSONObject json = new JSONObject();

        if (queryParams.containsKey("app_version") && !queryParams.get("app_version").isEmpty()) {
            int appVersion = Integer.parseInt(queryParams.get("app_version").get(0));

            if (appVersion >= webAppVersion) {
                String passFunction;
                try {
                    passFunction = md5.calculateMD5(password);
                    log.info("Calculated pass: {}", passFunction);
                } catch (NoSuchAlgorithmException e) {
                    json.put("success", false);
                    json.put("msg", "Неправильный пароль");
                    return Mono.just(json.toString());
                }

                return employeeRepository.findEmployeeByPassFunction(passFunction)
                        .flatMap(employeeOptional -> {
                            if (employeeOptional.isPresent()) {
                                Employee employee = employeeOptional.get();
                                json.put("success", true);
                                json.put("id", employee.id());
                                json.put("token", employee.token());
                                json.put("name", employee.name());
                                json.put("access", employee.access());
                                json.put("partner_id", employee.partnerId());
                            } else {
                                json.put("success", false);
                                json.put("msg", "Неправильный пароль");
                            }
                            return Mono.just(json.toString());
                        });
            } else {
                json.put("success", false);
                json.put("msg", "Обнови страницу или веб приложение.");
                return Mono.just(json.toString());
            }
        } else {
            json.put("success", false);
            json.put("msg", "Обнови страницу или веб приложение.");
            return Mono.just(json.toString());
        }
    }

    @Override
    public Mono<String> tokenCheck(Map<String, List<String>> queryParams) {
        String lat = queryParams.getOrDefault("lat", List.of("")).get(0);
        String lon = queryParams.getOrDefault("lon", List.of("")).get(0);

        if (lat.isEmpty() || lon.isEmpty() || lat.length() < 5 || lon.length() < 5) {
            return Mono.just(jsonResponse(false, "Требуется доступ к ГЕО данным."));
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
                                                        .map(response -> jsonResponse(true, newToken))
                                                );
                                    } else {
                                        return Mono.just(jsonResponse(false, "Твоя сессия устарела. Пожалуйста, авторизуйся заново."));
                                    }
                                });
                    }
                });
    }

    @Override
    public Mono<String> processLocationUpdate(String token, String lat, String lon) {
        return employeeRepository.getEmployeeOriginId(token)
                .flatMap(id -> {
                    double latitude = Double.parseDouble(lat);
                    double longitude = Double.parseDouble(lon);
                    return geoService.getAddressFromCoordinates(latitude, longitude)
                            .flatMap(address -> employeeRepository.updateEmployeeLocation(id, latitude, longitude, address))
                            .thenReturn(jsonResponse(true, token));
                })
                .onErrorResume(e -> Mono.just(jsonResponse(false, "Внутренняя ошибка сервера.")));
    }

    @Override
    public Mono<String> tokenGet(String relictToken) {
        return employeeRepository.getTokenByRelictToken(relictToken)
                .switchIfEmpty(Mono.defer(() -> {
                    JSONObject json = new JSONObject();
                    json.put("success", false);
                    json.put("msg", "Токен не найден");
                    return Mono.just(json.toString());
                }));
    }

    private String jsonResponse(boolean success, String message) {
        return "{\"success\":" + success + ", \"msg\":\"" + message + "\"}";
    }

}
