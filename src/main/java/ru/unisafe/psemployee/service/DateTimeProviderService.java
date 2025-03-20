package ru.unisafe.psemployee.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public interface DateTimeProviderService {


    String getStartOfDay();

    String getCurrentDateTime();

    String getStartOfSelectedMonth(int minusMonth);
}
