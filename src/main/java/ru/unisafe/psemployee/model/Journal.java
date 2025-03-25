package ru.unisafe.psemployee.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("journal")
public class Journal {
    @Id
    private Integer id;
    private String techPodName;
    private String problemType;
    private String problemText;
    private Boolean isYes;
    private LocalDateTime date;
    private String login;
}
