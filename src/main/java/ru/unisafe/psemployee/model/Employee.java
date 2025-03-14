package ru.unisafe.psemployee.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@Table("employee")
public class Employee {

    @Id
    private Integer id;
    private String token;
    private Integer access;
    private String name;
    private Integer partnerId;
    private Long chatId;
    private String userKey;
    private Integer visorId;
    private String passFunction;
    private Integer rrPlan;
    private Integer rrPrognosis;
    private Integer rrPercent;
    private Integer rrCuts;
    private Double lat;
    private Double lon;
    private String address;
    private String firebase;
    private String relictToken;

}
