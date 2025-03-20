package ru.unisafe.psemployee.dto.request;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StatisticsExcelRequest {
    @NotBlank(message = "Login cannot be empty")
    @Schema(description = "User login", example = "station2423")
    private String login;
    @NotBlank(message = "Request period cannot be empty")
    @Schema(description = "Request period in format: month_0, month_1, month_2", example = "month_0")
    private String time;
}
