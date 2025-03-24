package ru.unisafe.psemployee.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ru.unisafe.psemployee.dto.StationInformation;

import java.time.ZonedDateTime;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class StationInfoSupport implements StationInformation {
    private String login;
    private String stationCode;
    private String partnerId;
    private String partnerName;
    private String stationKey;
    private int visorId;
    private String visorName;
    private String address;
    private String ioInfo;
    private String comment;
    private boolean status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy HH:mm:ss", locale = "ru")
    private ZonedDateTime timeOpen;
    private String versionName;
    private int versionCode;
    private String subStatus;
    private int coupons;
    private String imei;
    private String sn;
    private String phone;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy HH:mm:ss", locale = "ru")
    private ZonedDateTime answerDate;
    private String plotterName;
    private String thisStatus;
    private String isHorizontal;
    private String isLaser;
    private String scaleX;
    private String scaleY;
    private String speed;
    private String pressure;

}
