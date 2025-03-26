package ru.unisafe.psemployee.repository.r2dbc;

import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import ru.unisafe.psemployee.model.StationInfo;
import ru.unisafe.psemployee.model.StationInfoExtended;
import ru.unisafe.psemployee.model.StationInfoSupport;
import ru.unisafe.psemployee.model.Tts;

import java.time.LocalDateTime;

@Repository
public interface TtsRepository extends R2dbcRepository<Tts, Integer> {

    @Query("SELECT id FROM tts ORDER BY id DESC LIMIT 1")
    Mono<Integer> getLastID();

    @Query("""
        SELECT
            tts.login,
            tts.station_code, tts.partner_id, tts.station_key, tts.visor_id,
            tts.address, tts.i_o_info, tts.comment, tts.status, tts.time_open,
            tts.version_name, tts.version_code, tts.sub_status, tts.sn, tts.imei,
            tts.is_problem, tts.problem_description,
            tts.phone, tts.lat, tts.lon, tts.is_cleaning,
            partners.partner_name AS partner_name,
            COALESCE(achievements.coupons, 0) AS coupons,
            persons.name AS visor_name
        FROM tts
        LEFT JOIN partners ON tts.partner_id = partners.id
        LEFT JOIN achievements ON tts.login = achievements.login
        LEFT JOIN persons ON tts.visor_id = persons.id
        WHERE tts.login = :login
    """)
    Mono<StationInfo> getStationInfo(String login);

    @Query("""
        WITH t1 AS (
            SELECT
                COUNT(CASE WHEN design_type = 1 THEN 1 END) AS tel,
                COUNT(CASE WHEN design_type = 2 THEN 1 END) AS lap,
                COUNT(CASE WHEN design_type = 3 THEN 1 END) AS wat
            FROM stats
            WHERE is_debugging = false
              AND login = :login
              AND moscow_time BETWEEN DATE_SUB(CURDATE(), INTERVAL 30 DAY) AND CURDATE()
        ),
        t2 AS (
            SELECT
                SUM(tel_gloss + tel_mat) AS sum_tel,
                SUM(lap_gloss + lap_mat) AS sum_lap,
                SUM(wat_gloss + wat_mat) AS sum_wat
            FROM store
            WHERE login = :login
        ),
        t3 AS (
            SELECT
                COALESCE(new_cuts_sales, 0) AS new_cuts_sales,
                COALESCE(cleaning_sales, 0) AS cleaning_sales,
                COALESCE(antivirus_sales, 0) AS antivirus_sales
            FROM ps_db.run_rate_edition
            WHERE login = :login
            ORDER BY id DESC
            LIMIT 1
        )
        SELECT
            tts.login,
            t1.tel, t1.lap, t1.wat,
            t2.sum_tel, t2.sum_lap, t2.sum_wat,
            COALESCE(t3.new_cuts_sales, 0) AS new_cuts_sales,
            COALESCE(t3.cleaning_sales, 0) AS cleaning_sales,
            COALESCE(t3.antivirus_sales, 0) AS antivirus_sales,
            tts.station_code, tts.partner_id, tts.station_key,
            tts.visor_id, tts.address, tts.i_o_info, tts.comment, tts.status,
            tts.lat, tts.lon, tts.time_open, tts.version_name, tts.version_code,
            tts.sub_status, tts.sn, tts.imei, tts.phone,
            tts.rr_plan, tts.rr_prognosis, tts.rr_percent, tts.rr_dynamic,
            tts.rr_defects, tts.rr_cuts, tts.act_build, tts.act_debuild,
            tts.act_fact, tts.plotter_name, partners.partner_name AS partner_name,
            COALESCE(achievements.coupons, 0) AS coupons, persons.name AS visor_name,
            latest_stats.moscow_time, latest_stats.design_id,
            latest_stats.design_parent, latest_stats.design_percent,
            latest_stats.element_id
        FROM t1, t2
        LEFT JOIN t3 ON TRUE
        LEFT JOIN tts ON tts.login = :login
        LEFT JOIN partners ON tts.partner_id = partners.id
        LEFT JOIN achievements ON tts.login = achievements.login
        LEFT JOIN persons ON tts.visor_id = persons.id
        LEFT JOIN (
            SELECT
                moscow_time, design_id, design_parent,
                design_percent, element_id
            FROM stats
            WHERE login = :login
            ORDER BY id DESC
            LIMIT 1
        ) AS latest_stats ON TRUE;
    """)
    Mono<StationInfoExtended> getStationMenuInfo(String login);

    @Query("""
    SELECT
    tts.login,
    tts.station_code,
    tts.partner_id,
    tts.station_key,
    tts.visor_id,
    tts.address,
    tts.i_o_info,
    tts.comment,
    tts.status,
    tts.time_open,
    tts.version_name,
    tts.version_code,
    tts.sub_status,
    tts.sn,
    tts.imei,
    tts.phone,
    partners.partner_name AS partner_name,
    COALESCE(achievements.coupons, 0) AS coupons,
    persons.name AS visor_name,
    support_firebase.plotter_name,
    support_firebase.this_status,
    support_firebase.is_horizontal,
    support_firebase.is_laser,
    support_firebase.scale_x,
    support_firebase.scale_y,
    support_firebase.speed,
    support_firebase.pressure,
    support_firebase.answer_date
    FROM tts
    LEFT JOIN partners ON tts.partner_id = partners.id
    LEFT JOIN achievements ON tts.login = achievements.login
    LEFT JOIN persons ON tts.visor_id = persons.id
    LEFT JOIN (SELECT login, plotter_name, this_status, is_horizontal, is_laser, scale_x, scale_y, speed, pressure, answer_date FROM support_firebase AS sf WHERE (login, answer_date) IN (SELECT login, MAX(answer_date) FROM support_firebase GROUP BY login)) AS support_firebase ON tts.login = support_firebase.login WHERE tts.login = :login;
""")
    Mono<StationInfoSupport> getStationInfoSupport(String login);

    Mono<Tts> getStationKeyByLogin(String login);

    @Modifying
    @Query("UPDATE tts SET problem_solved_date = :problemSolvedDate WHERE login = :login")
    Mono<Void> updateProblemSolvedDateByLogin(@Param("login") String login, @Param("problemSolvedDate") LocalDateTime problemSolvedDate);
}
