package ru.unisafe.psemployee.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table(name = "web_visiting")
@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WebVisiting {

    @Id
    private Long id;

    private String login;

    @Column("station_code")
    private String stationCode;

    @Column("visor_id")
    private Integer visorId;

    @Column("visor_name")
    private String visorName;

    private String address;

    @Column("partner_id")
    private Integer partnerId;

    @Column("partner_name")
    private String partnerName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy HH:mm:ss", locale = "ru")
    private LocalDateTime created;

    private Integer distance;

    private Double lat;

    private Double lon;

    @Column("visit_address")
    private String visitAddress;

    @Column("is_defect_gathering")
    private Boolean isDefectGathering;

    @Column("is_problem_solved")
    private Boolean isProblemSolved;

    @Column("problem_description")
    private String problemDescription;

    private String comment;
}
