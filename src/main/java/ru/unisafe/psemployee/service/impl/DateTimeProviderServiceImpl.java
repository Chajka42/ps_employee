package ru.unisafe.psemployee.service.impl;

import org.springframework.stereotype.Service;
import ru.unisafe.psemployee.service.DateTimeProviderService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class DateTimeProviderServiceImpl implements DateTimeProviderService {

    @Override
    public String getStartOfDay() {
        LocalDate currentDate = LocalDate.now();
        LocalDateTime startOfDay = currentDate.atStartOfDay();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return startOfDay.format(formatter);
    }

    @Override
    public String getCurrentDateTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return now.format(formatter);
    }

    @Override
    public String getStartOfSelectedMonth(int minusMonth) {
        LocalDate currentDate = LocalDate.now();
        LocalDate firstDayOfSelectedMonth = currentDate.withDayOfMonth(1).minusMonths(minusMonth);
        LocalDateTime startOfSelectedMonth = firstDayOfSelectedMonth.atStartOfDay();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return startOfSelectedMonth.format(formatter);
    }
}
