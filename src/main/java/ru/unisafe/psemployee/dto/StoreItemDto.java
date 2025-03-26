package ru.unisafe.psemployee.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class StoreItemDto {
    private int id;
    private String name;
    private String teg;
    private int value;
}
