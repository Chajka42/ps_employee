package ru.unisafe.psemployee.repository.r2dbc;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import ru.unisafe.psemployee.model.StoreItem;

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
}
