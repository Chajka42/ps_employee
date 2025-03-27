package ru.unisafe.psemployee.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Table("web_requests")
@Data
public class WebRequest {
    @Id
    private Integer id;
    private String login;
    private String stationCode;
    private Integer visorId;
    private String visorName;
    private String address;
    private LocalDateTime created;
    private String sdekId;
    private LocalDateTime completed;
    private Boolean isCompleted;
    private String directionType;
    private String agregatorType;
    private String comment;
    private Integer partnerId;
    private String partnerName;
    private Boolean isReceived;
    private Boolean isBoxed;
    private LocalDateTime wasEdited;
    private Boolean see;
    private LocalDateTime wasReceived;
    private String storePhoto;
    private String boxedPerson;
}
