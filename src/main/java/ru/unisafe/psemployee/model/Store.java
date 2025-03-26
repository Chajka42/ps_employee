package ru.unisafe.psemployee.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("store")
@Data
public class Store {
    @Id
    private Integer id;
    private String login;
    private String visorName;
    private Integer visorId;

    private Integer racelPlastic;
    private Integer racelRubber;
    private Integer spray;
    private Integer cloth;
    private Integer pincer;
    private Integer carpet;
    private Integer hairdryer;
    private Integer carrierBig;
    private Integer carrierLil;
    private Integer knifeCameo;
    private Integer knifeHolder;
    private Integer knifeBlade;

    private Integer telGloss;
    private Integer telMat;
    private Integer telSpy;
    private Integer telColor;

    private Integer lapGloss;
    private Integer lapMat;
    private Integer lapColor;

    private Integer watGloss;
    private Integer watMat;
    private Integer watColor;

    private Integer defTelGloss;
    private Integer defTelMat;
    private Integer defTelSpy;
    private Integer defTelColor;

    private Integer defLapGloss;
    private Integer defLapMat;
    private Integer defLapColor;

    private Integer defWatGloss;
    private Integer defWatMat;
    private Integer defWatColor;

    private Float rollGloss;
    private Float rollMate;
}
