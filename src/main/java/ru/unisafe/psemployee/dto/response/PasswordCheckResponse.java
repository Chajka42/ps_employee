package ru.unisafe.psemployee.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import ru.unisafe.psemployee.dto.EmployeeDto;

@Getter
@Setter
@Schema(description = "Ответ при проверке пароля")
public class PasswordCheckResponse extends BaseResponse {

    @Schema(description = "Данные сотрудника")
    private EmployeeDto employee;

    public PasswordCheckResponse(boolean success, String msg, EmployeeDto employee) {
        super(success, msg);
        this.employee = employee;
    }

}
