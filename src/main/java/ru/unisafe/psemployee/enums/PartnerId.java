package ru.unisafe.psemployee.enums;

import lombok.Getter;

@Getter
public enum PartnerId {

    MTS(3),
    MEGAFON(5),
    EVRIKA(7),
    MVIDEO(19),
    SVYAZON(23);

    private int partnerId;

    PartnerId(int partnerId) {
        this.partnerId = partnerId;
    }

    public static PartnerId fromId(int id) {
        for (PartnerId partner : values()) {
            if (partner.getPartnerId() == id) {
                return partner;
            }
        }
        throw new IllegalArgumentException("Unknown partner ID: " + id);
    }
}
