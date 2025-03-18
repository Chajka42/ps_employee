package ru.unisafe.psemployee.enums;

import lombok.Getter;

@Getter
public enum PartnerEnum {

    UNISAFE(1),
    UNKNOWN(2),
    MTS(3),
    SVYAZNOY(4),
    MEGAFON(5),
    SULPAK(6),
    EVRIKA(7),
    ISPACE(8),
    ALSER(9),
    MECHTA(10),
    BEELINE(11),
    TECHNODOM(12),
    FORA_KZ(13),
    FREEDOM_MOBILE(14),
    KCELL_STORE(15),
    DNS_SHOP(16),
    TELE2(17),
    KASPIY(18),
    MVIDEO(19),
    MAKSIMUS(20),
    FRANCHIZA(21),
    PEDANT(22),
    SVYAZ_ON(23),
    MTS_BANK(24),
    NBCOM(25);

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
