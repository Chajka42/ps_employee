package ru.unisafe.psemployee.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class ValueParser {

    public Mono<Object> parseValue(String type, String value) {
        try {
            Object parsedValue = switch (type) {
                case "bool" -> parseBoolean(value);
                case "int" -> parseInteger(value);
                case "string" -> value;
                default -> throw new IllegalArgumentException("Неизвестный тип: " + type);
            };
            return Mono.just(parsedValue);
        } catch (IllegalArgumentException e) {
            log.warn("Ошибка парсинга: {}", e.getMessage());
            return Mono.error(e);
        }
    }

    private Boolean parseBoolean(String value) {
        if ("true".equalsIgnoreCase(value)) return true;
        if ("false".equalsIgnoreCase(value)) return false;
        throw new IllegalArgumentException("Булево значение должно быть 'true' или 'false'");
    }

    private Integer parseInteger(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Некорректное числовое значение: " + value);
        }
    }
}
