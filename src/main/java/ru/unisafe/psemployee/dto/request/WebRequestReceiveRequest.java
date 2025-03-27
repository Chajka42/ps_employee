package ru.unisafe.psemployee.dto.request;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class WebRequestReceiveRequest {
    @Min(value = 1, message = "requestId не может быть меньше 1")
    private Long requestId;
}
