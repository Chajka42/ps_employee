package ru.unisafe.psemployee.dto;

import lombok.Data;

@Data
public class CategoryDto {
    private long id;
    private long categoryId;
    private String categoryName;
    private String name;
    private String value;
    private String storeTeg;
}
