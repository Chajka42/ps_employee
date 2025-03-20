package ru.unisafe.psemployee.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestWithStationLogin {
    @NotBlank(message = "Login cannot be empty")
    private String login;
}
