package ru.unisafe.psemployee.repository.r2dbc;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import ru.unisafe.psemployee.model.WebItem;

@Repository
public interface WebItemRepository extends R2dbcRepository<WebItem, Long> {

    @Query("""
                SELECT item_id, item_value
                FROM ps_db.web_items
                WHERE request_id = :requestId AND is_to_tt = false
            """)
    Flux<WebItem> findItemsByRequestId(@Param("requestId") Long requestId);


}
