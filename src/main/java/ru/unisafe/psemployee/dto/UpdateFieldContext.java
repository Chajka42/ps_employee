package ru.unisafe.psemployee.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.unisafe.psemployee.dto.request.ChangeFieldRequest;

@Data
@AllArgsConstructor
public class UpdateFieldContext {
    private String table;
    private String whereField;
    private ChangeFieldRequest request;
    private Object parsedValue;
}
