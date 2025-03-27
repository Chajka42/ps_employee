package ru.unisafe.psemployee.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AddStationNoteRequest {
    @NotBlank(message = "Токен не может быть пустым")
    private String token;
    @NotBlank(message = "Заметка не может быть пустой")
    private String note;
    @NotBlank(message = "Логин станции не может быть пустым")
    private String login;
}
