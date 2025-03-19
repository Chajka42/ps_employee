package ru.unisafe.psemployee.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.unisafe.psemployee.service.DateTimeFormatterService;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
public class DateTimeFormatterServiceImpl implements DateTimeFormatterService {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public String convertToLocalDateTime(Object date) {
        if (date instanceof ZonedDateTime zonedDateTime) {
            return zonedDateTime.toLocalDateTime().format(FORMATTER);
        } else if (date instanceof LocalDateTime localDateTime) {
            return localDateTime.format(FORMATTER);
        } else {
            log.warn("Неизвестный формат даты: {}", date);
            return "unknown";
        }
    }
}
