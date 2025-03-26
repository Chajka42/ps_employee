package ru.unisafe.psemployee.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("store_items")
public class StoreItem {
    @Id
    private Integer id;

    private Integer categoryId;
    private String categoryName;
    private String name;
    private Integer value;
    private String storeTeg;
}
