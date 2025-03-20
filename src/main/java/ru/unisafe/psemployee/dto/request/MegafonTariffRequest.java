package ru.unisafe.psemployee.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MegafonTariffRequest {
    @JsonProperty("sale_key")
    @NotBlank(message = "Sale key cannot be empty")
    private String saleKey;
}
