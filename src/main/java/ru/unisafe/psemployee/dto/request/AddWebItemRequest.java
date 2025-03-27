package ru.unisafe.psemployee.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddWebItemRequest {
    @Min(value = 1, message = "ID запроса должен быть больше 0")
    private Long requestId;

    @NotNull(message = "Флаг isToTt должен быть указан")
    private Boolean isToTt;

    @Min(value = 1, message = "ID товара должен быть больше 0")
    private int itemId;

    @NotBlank(message = "Название товара не должно быть пустым")
    private String itemName;

    @NotBlank(message = "Тег товара не должен быть пустым")
    private String itemTeg;

    @NotBlank(message = "Логин станции не должен быть пустым")
    private String login;

    @Min(value = 1, message = "Количество товара должно быть больше 0")
    private int itemValue;
}
