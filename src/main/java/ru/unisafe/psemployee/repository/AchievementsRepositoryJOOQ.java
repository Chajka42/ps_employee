package ru.unisafe.psemployee.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.unisafe.psemployee.dto.request.ChangeCouponsRequest;
import ru.unisafe.psemployee.dto.response.BaseResponse;
import ru.unisafe.psemployee.dto.response.CouponsInfoResponse;

import java.time.LocalDateTime;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;

@Slf4j
@RequiredArgsConstructor
@Repository
public class AchievementsRepositoryJOOQ {

    private final DSLContext dsl;

    public Mono<CouponsInfoResponse> fetchCouponsInfo(String login) {
        return Mono.from(dsl.select(
                                field("coupons", Integer.class),
                                field("coupons_earned_ach", Integer.class),
                                field("coupons_earned_cuts", Integer.class))
                        .from(table("achievements"))
                        .where(field("login").eq(login)))
                .map(record -> CouponsInfoResponse.builder()
                        .success(true)
                        .coupons(record.get("coupons", Integer.class))
                        .couponsEarnedAch(record.get("coupons_earned_ach", Integer.class))
                        .couponsEarnedCuts(record.get("coupons_earned_cuts", Integer.class))
                        .build());
    }

    public Flux<CouponsInfoResponse.CouponChange> fetchCouponChangeList(String login) {
        return Flux.from(dsl.select(
                                field("id", Integer.class),
                                field("coupons", Integer.class),
                                field("description", String.class),
                                field("created", LocalDateTime.class),
                                field("new_total", Integer.class))
                        .from(table("achievements_logs"))
                        .where(field("login").eq(login))
                        .orderBy(field("id").desc())
                        .limit(100))
                .map(record -> CouponsInfoResponse.CouponChange.builder()
                        .id(record.get("id", Integer.class))
                        .coupons(record.get("coupons", Integer.class))
                        .description(record.get("description", String.class))
                        .created(record.get("created", LocalDateTime.class))
                        .newTotal(record.get("new_total", Integer.class))
                        .build());
    }

    public Mono<BaseResponse> changeCoupons(ChangeCouponsRequest request) {
        log.info("Request positive or negative: {}", request.getIncrease());
        return Mono.from(dsl.select(field("coupons", Integer.class))
                        .from(table("achievements"))
                        .where(field("login").eq(request.getLogin())))
                .flatMap(record -> {
                    int currentCoupons = record.get("coupons", Integer.class);
                    boolean isAdding = Boolean.TRUE.equals(request.getIncrease());
                    log.info("isIncrease after Boolean.TRUE check: {}", isAdding);

                    int newCoupons = isAdding
                            ? currentCoupons + request.getValue()
                            : currentCoupons - request.getValue();

                    log.info("New coupons after calculation: {}", newCoupons);
                    if (!isAdding && newCoupons < 0) {
                        log.warn("Attempt to subtract more coupons than available!");
                        return Mono.just(new BaseResponse(false, "Недостаточно купонов для списания"));
                    }

                    return Mono.from(dsl.update(table("achievements"))
                                    .set(field("coupons"), newCoupons)
                                    .where(field("login").eq(request.getLogin())))
                            .then(saveAchievementsLog(request.getLogin(), request.getValue(), newCoupons,
                                    isAdding ? "Начисление бонуса" : "Списание купонов"))
                            .thenReturn(new BaseResponse(true, "Купоны успешно обновлены"));
                })
                .defaultIfEmpty(new BaseResponse(false, "Станция не найдена"))
                .doOnError(error -> log.error("Ошибка при изменении купонов: {}", error.getMessage()));
    }

    public Mono<Void> saveAchievementsLog(String login, int value, int total, String description) {
        return Mono.from(dsl.insertInto(table("achievements_logs"))
                        .columns(field("login"), field("coupons"), field("description"), field("new_total"))
                        .values(login, value, description, total))
                .then();
    }
}

