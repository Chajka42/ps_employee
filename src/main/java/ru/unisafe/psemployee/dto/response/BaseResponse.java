package ru.unisafe.psemployee.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@ToString
@Schema(description = "Базовый ответ")
public class BaseResponse {

    @Schema(description = "Флаг успешного выполнения", example = "true")
    private boolean success;

    @Schema(description = "Сообщение об ошибке или статусе", example = "Пароль верный")
    private String message;

    public BaseResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

}
