package ru.unisafe.psemployee.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MasterKeyRequest {
    @NotBlank(message = "Token cannot be empty")
    private String token;
    @NotBlank(message = "Station login cannot be empty")
    private String login;
}
