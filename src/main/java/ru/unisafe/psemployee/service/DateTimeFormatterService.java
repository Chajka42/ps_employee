package ru.unisafe.psemployee.service;

import java.time.LocalDateTime;

public interface DateTimeFormatterService {
    String convertToLocalDateTime(Object date);

    String formatToRussianDateTime(LocalDateTime dateTime);
}
