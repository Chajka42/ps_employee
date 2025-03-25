package ru.unisafe.psemployee.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Getter
@Setter
@Table("geo_marks")
public class GeoMark {
    @Id
    private Integer id;

    @Column("employee_id")
    private int employeeId;

    @Column("employee_name")
    private String employeeName;

    private double lat;
    private double lon;
    private double distance;
    private String login;

    @Column("created")
    private LocalDateTime created;
}
