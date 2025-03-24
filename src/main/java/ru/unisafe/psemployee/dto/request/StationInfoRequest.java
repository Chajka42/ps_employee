package ru.unisafe.psemployee.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class StationInfoRequest {
    @NotBlank(message = "Логин станции не может быть пустым")
    private String login;
}
