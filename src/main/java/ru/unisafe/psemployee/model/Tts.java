package ru.unisafe.psemployee.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@Table("tts")
public class Tts {

    @Id
    private Integer id;
    @Column("station_key")
    private String stationKey;
    private String token;
    @Column("station_code")
    private String stationCode;
    private String login;
    private String pass;
    @Column("partner_id")
    private Integer partnerId;
    @Column("plotter_id")
    private Integer plotterId;
    @Column("visor_id")
    private Integer visorId;
    @Column("region_unisafe_id")
    private Integer regionUnisafeId;
    @Column("region_partner_id")
    private Integer regionPartnerId;
    private String address;
    @Column("i_o_info")
    private String ioInfo;
    private String comment;
    private Boolean status;
    @Column("n_learning")
    private Integer nLearning;
    @Column("n_debugging")
    private Integer nDebugging;
    @Column("time_open")
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
