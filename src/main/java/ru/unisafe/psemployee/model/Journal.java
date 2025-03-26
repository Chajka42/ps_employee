package ru.unisafe.psemployee.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("journal")
public class Journal {
    @Id
    private Integer id;
    private Long techPodId;
    private String techPodName;
    private String login;
    private Integer partnerId;
    private String partnerCode;
    private Integer managerId;
    private String managerName;
    private String problemType;
    private String problemText;
    private Boolean isYes;
    private Boolean isManagerRequired;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy HH:mm:ss", locale = "ru")
    private ZonedDateTime date;
}
