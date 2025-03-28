package ru.unisafe.psemployee.repository.r2dbc;

import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.unisafe.psemployee.dto.ItemRequestBeforeDto;
import ru.unisafe.psemployee.dto.OtherRequestDto;
import ru.unisafe.psemployee.dto.WebRequestDto;
import ru.unisafe.psemployee.model.WebRequest;

@Repository
public interface WebRequestRepository extends R2dbcRepository<WebRequest, Long> {

    @Query("""
                SELECT *
                FROM web_requests
                WHERE login = :login
                ORDER BY id DESC
                LIMIT 20
            """)
    Flux<WebRequest> findLatestByLogin(String login);

    @Modifying
    @Query("""
                UPDATE web_requests
                SET is_received = true, was_received = NOW()
                WHERE id = :requestId
            """)
    Mono<Integer> markAsReceived(@Param("requestId") Long requestId);

    @Modifying
    @Query("UPDATE web_requests SET was_edited = NOW() WHERE id = :requestId")
    Mono<Void> updateWasEdited(@Param("requestId") Long requestId);

    @Query("UPDATE web_requests w SET see = true WHERE id = :requestId")
    @Modifying
    Mono<Integer> updateWebRequestSetSeeTrue(@Param("requestId") Long requestId);

    @Query("""
            SELECT
                web_requests.id,
                web_requests.login,
                web_requests.station_code,
                web_requests.visor_id,
                web_requests.visor_name,
                web_requests.address,
                web_requests.created,
                web_requests.sdek_id,
                web_requests.completed AS completed_date,
                web_requests.is_completed AS completed,
                web_requests.direction_type,
                web_requests.agregator_type,
                web_requests.comment,
                web_requests.partner_id,
                web_requests.partner_name,
                web_requests.is_boxed,
                web_requests.was_edited,
                web_requests.is_received,
                web_requests.was_received,
                web_requests.store_photo,
                web_requests.boxed_person,
                tts.lat, tts.lon, tts.plotter_name
                FROM web_requests
                LEFT JOIN tts ON web_requests.login = tts.login
                WHERE web_requests.id = ?
            """)
    Flux<WebRequestDto> getWebRequestPlusTtsInfo(@Param("requestId") Long requestId);

    @Query("SELECT id, created FROM web_requests WHERE login = :login AND id != :requestId")
    Flux<OtherRequestDto> selectOtherRequests(@Param("requestId") Long requestId, @Param("login") String login);

    @Query("""
            SELECT (SELECT MAX(wr.created)
                    FROM web_requests wr
                        JOIN web_items wi ON wr.id = wi.request_id
                    WHERE wi.item_teg = 'spray' AND wr.login = :login) AS last_spray_request,
                (SELECT MAX(wr.created)
                 FROM web_requests wr
                     JOIN web_items wi ON wr.id = wi.request_id
                 WHERE wi.item_teg = 'cloth' AND wr.login = :login) AS last_cloth_request,
                (SELECT MAX(wr.created)
                 FROM web_requests wr
                     JOIN web_items wi ON wr.id = wi.request_id
                 WHERE wi.item_teg like 'carrier%' AND wr.login = :login) AS last_carrier_request,
                (SELECT MAX(created) FROM web_requests WHERE login = :login) AS last_request
            """)
    Flux<ItemRequestBeforeDto> selectItemsRequestedBefore(@Param("login") String login);
}
