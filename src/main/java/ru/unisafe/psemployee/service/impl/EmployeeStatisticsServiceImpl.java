package ru.unisafe.psemployee.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.unisafe.psemployee.dto.StatisticRecord;
import ru.unisafe.psemployee.dto.request.RequestWithStationLogin;
import ru.unisafe.psemployee.dto.request.StatisticsExcelRequest;
import ru.unisafe.psemployee.repository.StatisticsRepositoryJOOQ;
import ru.unisafe.psemployee.service.DateTimeFormatterService;
import ru.unisafe.psemployee.service.DateTimeProviderService;
import ru.unisafe.psemployee.service.EmployeeStatisticsService;

import java.io.ByteArrayOutputStream;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class EmployeeStatisticsServiceImpl implements EmployeeStatisticsService {

    private final StatisticsRepositoryJOOQ statisticRepository;
    private final DateTimeProviderService dateTimeProviderService;
    private final DateTimeFormatterService dateTimeFormatterService;

    @Override
    public Mono<ResponseEntity<byte[]>> getStatisticsExcel(StatisticsExcelRequest request) {
        String login = request.getLogin();
        String start = switch (request.getTime()) {
            case "month_0" -> dateTimeProviderService.getStartOfSelectedMonth(0);
            case "month_1" -> dateTimeProviderService.getStartOfSelectedMonth(1);
            case "month_2" -> dateTimeProviderService.getStartOfSelectedMonth(2);
            default -> dateTimeProviderService.getStartOfDay();
        };
        String end = dateTimeProviderService.getCurrentDateTime();

        return statisticRepository.fetchStatistics(login, start, end)
                .collectList()
                .flatMap(this::generateExcel)
                .map(bytes -> ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=ps_stat_" + login + ".xlsx")
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .body(bytes));
    }

    @Override
    public Flux<StatisticRecord> getStatistic(RequestWithStationLogin request) {
        return statisticRepository.fetchStatistics(request.getLogin());
    }

    private Mono<byte[]> generateExcel(List<StatisticRecord> records) {
        return Mono.fromCallable(() -> {
            try (Workbook workbook = new XSSFWorkbook()) {
                Sheet sheet = workbook.createSheet("STAT");
                Row headerRow = sheet.createRow(0);
                String[] headers = {"Рез", "Время МСК", "Местное", "Тип модели", "Бренд", "Модель", "Лекало", "id лекала", "Пленка", "Приблизительное местоположение", "Чек"};
                for (int i = 0; i < headers.length; i++) {
                    headerRow.createCell(i).setCellValue(headers[i]);
                }

                int rowNum = 1;
                for (StatisticRecord record : records) {
                    Row row = sheet.createRow(rowNum++);

                    if (record.defect()) {
                        row.createCell(0).setCellValue("брак");
                    } else if (record.isLearning()) {
                        row.createCell(0).setCellValue("обучение");
                    } else if (record.isDebugging()) {
                        row.createCell(0).setCellValue("отладка");
                    } else if (record.isGuarantee()) {
                        row.createCell(0).setCellValue("гарантия");
                    } else {
                        row.createCell(0).setCellValue("продажа");
                    }

                    row.createCell(1).setCellValue(dateTimeFormatterService.formatToRussianDateTime(record.moscowTime()));
                    row.createCell(2).setCellValue(record.timeZone());

                    switch (record.designType()) {
                        case 1 -> row.createCell(3).setCellValue("Телефон");
                        case 2 -> row.createCell(3).setCellValue("Планшет");
                        default -> row.createCell(3).setCellValue("Часы");
                    }

                    row.createCell(4).setCellValue(record.designId());
                    row.createCell(5).setCellValue(record.designParent());
                    row.createCell(6).setCellValue(record.designPercent());
                    row.createCell(7).setCellValue(record.elementId());

                    switch (record.filmType()) {
                        case "GLOSSY" -> row.createCell(8).setCellValue("Глянцевая");
                        case "MATTE" -> row.createCell(8).setCellValue("Матовая");
                        case "COLOR" -> row.createCell(8).setCellValue("Цветная");
                        default -> row.createCell(8).setCellValue("Антишпион");
                    }

                    row.createCell(9).setCellValue(record.geoLocation());
                    row.createCell(10).setCellValue(record.sale());
                }

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                workbook.write(outputStream);
                return outputStream.toByteArray();
            }
        });
    }

}
