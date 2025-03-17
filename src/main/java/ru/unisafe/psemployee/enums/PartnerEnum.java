package ru.unisafe.psemployee.enums;

import lombok.Getter;

@Getter
public enum PartnerEnum {

    MTS(3),
    MEGAFON(5),
    EVRIKA(7),
    MVIDEO(19),
    SVYAZON(23);

    private int partnerId;

    PartnerEnum(int partnerId) {
        this.partnerId = partnerId;
    }

    public static PartnerEnum fromId(int id) {
        for (PartnerEnum partner : values()) {
            if (partner.getPartnerId() == id) {
                return partner;
            }
        }
        throw new IllegalArgumentException("Unknown partner ID: " + id);
    }
}
