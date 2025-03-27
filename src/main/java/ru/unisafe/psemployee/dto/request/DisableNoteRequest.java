package ru.unisafe.psemployee.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DisableNoteRequest {
    @NotNull(message = "Id заметки не может быть пустым")
    private long noteId;
}
