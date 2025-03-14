package ru.unisafe.psemployee.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Getter
@Setter
@Table("tts")
public class Tts {

    @Id
    private Integer id;
    private String stationKey;
    private String token;
    private String stationCode;
    private String login;
    private String pass;
    private Integer partnerId;
    private Integer plotterId;
    private Integer visorId;
    private Integer regionUnisafeId;
    private Integer regionPartnerId;
    private String address;
    private String iOInfo;
    private String comment;
    private Boolean status;
    private Integer nLearning;
    private Integer nDebugging;
    private LocalDateTime timeOpen;
    private LocalDateTime timeClose;
    private String versionName;
    private Integer versionCode;
    private Integer prognosis;
    private LocalDateTime prognosisDate;
    private String fcmToken;
    private Double lat;
    private Double lon;
    private String subStatus;
    private String actBuild;
    private String actDebuild;
    private String actFact;
    private Integer officialSalesValue;
    private LocalDateTime officialSalesFrom;
    private LocalDateTime officialSalesTo;
    private Boolean isRrOn;
    private Integer lastRank;
    private Integer currentRank;
    private Integer ratingGroup;
    private String sn;
    private String imei;
    private String phone;
    private Boolean isProblem;
    private String problemDescription;
    private LocalDateTime problemSolvedDate;
    private Integer rrPlan;
    private Integer rrPrognosis;
    private Integer rrPercent;
    private Integer rrDynamic;
    private Integer rrDefects;
    private Integer rrCuts;
    private String plotterName;
    private Boolean isCleaning;

}
