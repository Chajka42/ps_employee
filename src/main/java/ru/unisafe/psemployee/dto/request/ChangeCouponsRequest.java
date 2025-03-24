package ru.unisafe.psemployee.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ChangeCouponsRequest {
    @NotBlank(message = "Login cannot be empty")
    private String login;
    @Min(value = 1, message = "value must be greater than 0")
    private int value;
    @NotNull(message = "You must provide false or true")
    private Boolean increase;
}
