package ru.unisafe.psemployee.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeFieldRequest {
    @NotBlank(message = "Login cannot be empty")
    private String login;
    @NotBlank(message = "field cannot be empty")
    private String field;
    @NotBlank(message = "type cannot be empty")
    private String type;
    @NotBlank(message = "value cannot be empty")
    private String value;
}
