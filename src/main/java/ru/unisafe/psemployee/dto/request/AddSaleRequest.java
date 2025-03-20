package ru.unisafe.psemployee.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddSaleRequest {
    @NotBlank(message = "Login cannot be empty")
    private String login;
    @NotBlank(message = "Code cannot be empty")
    private String code;
    @Min(value = 1, message = "Partner ID must be greater than 0")
    private int partnerId;
    @Min(value = 0, message = "Type must be positive")
    private int type;
    @Min(value = 0, message = "Model must be positive")
    private int model;
}
