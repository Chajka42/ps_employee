package ru.unisafe.psemployee.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class WebRequestDto {
    private int id;
    private String login;
    private String stationCode;
    private int visorId;
    private String visorName;
    private String address;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy HH:mm:ss", locale = "ru")
    private ZonedDateTime created;
    private String sdekId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy HH:mm:ss", locale = "ru")
    private ZonedDateTime completedDate;
    private Boolean completed;
    private String directionType;
    private String agregatorType;
    private String comment;
    private int partnerId;
    private String partnerName;
    private boolean isBoxed;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy HH:mm:ss", locale = "ru")
    private ZonedDateTime wasEdited;
    private boolean isReceived;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy HH:mm:ss", locale = "ru")
    private ZonedDateTime wasReceived;
    private String storePhoto;
    private String boxedPerson;
    private double lat;
    private double lon;
    private String plotterName;
}
