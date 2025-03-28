package ru.unisafe.psemployee.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GetRequestInfoRequest {
    @Min(value = 1, message = "Id реквеста должен быть больше 0")
    private int requestId;

    @NotNull(message = "Поле see обязательно к заполнению")
    private boolean see;
}
