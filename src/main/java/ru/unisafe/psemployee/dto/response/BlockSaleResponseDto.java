package ru.unisafe.psemployee.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlockSaleResponseDto {
    private boolean success;
    private List<MessageDto> data;
}
