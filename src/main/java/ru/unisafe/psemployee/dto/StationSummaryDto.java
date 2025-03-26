package ru.unisafe.psemployee.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class StationSummaryDto {
    private int telGloss;
    private int telMat;
    private int telSpy;
    private int telColor;
    private int lapColor;
    private int lapGloss;
    private int lapMat;
    private int watColor;
    private int watGloss;
    private int watMat;
    private int telSum;
    private int lapSum;
    private int watSum;
    private int defTelGloss;
    private int defTelMat;
    private int defTelSpy;
    private int defTelColor;
    private int defLapColor;
    private int defLapGloss;
    private int defLapMat;
    private int defWatColor;
    private int defWatGloss;
    private int defWatMat;
    private int defTotal;
}
