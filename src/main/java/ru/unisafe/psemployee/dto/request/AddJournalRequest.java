package ru.unisafe.psemployee.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddJournalRequest {
    @NotBlank(message = "Логин не должен быть пустым")
    private String login;

    @NotBlank(message = "Код не должен быть пустым")
    private String code;

    @NotBlank(message = "Описание проблемы не должно быть пустым")
    @Size(min = 13, message = "Описание слишком короткое")
    @Size(max = 250, message = "Описание слишком длинное")
    private String problemText;

    @NotBlank(message = "Тип проблемы обязателен")
    private String problemType;

    @NotNull(message = "Статус обязателен (true или false, решено или нет)")
    private Boolean resolved;

    @Min(value = 1, message = "ID визора должен быть положительным числом")
    private int visorId;

    @Min(value = 1, message = "ID партнера должен быть положительным числом")
    private int partnerId;

    @NotBlank(message = "Имя визора не должно быть пустым")
    private String visorName;

    @NotBlank(message = "Токен обязателен")
    private String token;
}
