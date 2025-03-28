package ru.unisafe.psemployee.model;

import lombok.*;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table("web_items")
public class WebItem {
    private Long id;
    private Long requestId;
    @Column("is_to_tt")
    private boolean isToTt;
    private int itemId;
    private String itemName;
    private String itemTeg;
    private String login;
    private int itemValue;
    private Long visitId;
}
