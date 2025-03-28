package ru.unisafe.psemployee.repository.r2dbc;

import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.unisafe.psemployee.dto.CategoryDto;
import ru.unisafe.psemployee.model.StoreItem;

@Repository
public interface StoreItemsRepository extends R2dbcRepository<StoreItem, Long> {
    @Query("""
        SELECT si.*, sc.name AS category_name
        FROM store_items si
        INNER JOIN store_categories sc ON si.category_id = sc.id
        WHERE si.status = true
        AND (si.partner_id = :partnerId OR si.partner_id = 0)
        ORDER BY si.id DESC
    """)
    Flux<StoreItem> findByPartnerId(int partnerId);

    @Modifying
    @Query("""
        UPDATE store_items
        SET value = value + :itemValue
        WHERE id = :itemId
    """)
    Mono<Void> updateItemValue(@Param("itemId") Integer itemId, @Param("itemValue") Integer itemValue);

    @Query(
            "SELECT store_items.*, " +
                    "store_categories.name AS category_name " +
                    "FROM store_items " +
                    "INNER JOIN store_categories ON store_items.category_id = store_categories.id " +
                    "WHERE store_items.status = true AND (store_items.partner_id = :partnerId OR store_items.partner_id = 0)"
    )
    Flux<CategoryDto> selectFromStoreItemsAndStoreCategories(@Param("partnerId") Long partnerId);
}
