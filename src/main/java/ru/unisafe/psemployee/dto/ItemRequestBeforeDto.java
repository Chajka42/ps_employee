package ru.unisafe.psemployee.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class ItemRequestBeforeDto {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy HH:mm:ss", locale = "ru")
    private ZonedDateTime lastSprayRequest;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy HH:mm:ss", locale = "ru")
    private ZonedDateTime lastClothRequest;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy HH:mm:ss", locale = "ru")
    private ZonedDateTime lastCarrierRequest;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy HH:mm:ss", locale = "ru")
    private ZonedDateTime lastRequest;
}
