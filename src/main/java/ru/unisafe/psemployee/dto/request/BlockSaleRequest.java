package ru.unisafe.psemployee.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlockSaleRequest {
    @NotNull(message = "ID is required")
    private Integer id;
    @NotNull(message = "Partner ID is required")
    private Integer partnerId;
}
