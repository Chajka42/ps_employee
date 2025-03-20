package ru.unisafe.psemployee.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ChangeCouponsRequest {
    private String login;
    private int value;
    private boolean isPlus;
}
