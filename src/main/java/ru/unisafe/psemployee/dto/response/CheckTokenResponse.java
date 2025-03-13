package ru.unisafe.psemployee.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;


@Schema(description = "Ответ при проверке токена")
@Getter
@Setter
public class CheckTokenResponse extends BaseResponse {

    @Schema(description = "Обновленный токен", example = "new_token_123")
    private String token;

    public CheckTokenResponse(boolean success, String msg, String token) {
        super(success, msg);
        this.token = token;
    }

}
