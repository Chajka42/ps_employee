package ru.unisafe.psemployee.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@Table("partners")
public class Partner {
    @Id
    private Integer id;
    private String partnerName;
    private Boolean promotionalCodeMode;
    private Boolean receiptMode;
    private Boolean personIdMode;
    private Boolean filmQrMode;
    private Boolean couponMode;
}
