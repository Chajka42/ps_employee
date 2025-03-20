package ru.unisafe.psemployee.repository;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.unisafe.psemployee.dto.response.CouponsInfoResponse;

import java.time.LocalDateTime;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;

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
}

//public static void getCouponsInfo(Map<String, List<String>> queryParams, ChannelHandlerContext ctx) {
//        String login = queryParams.get("login").get(0);
//        DatabaseService dbService = new DatabaseService(Constants.URL_DB, Constants.USER_DB, Constants.PASSWORD_DB);
//        PreparedStatement statement;
//        Connection connection;
//
//        ObjectNode responseNode = JsonNodeFactory.instance.objectNode();
//
//        try {
//            connection = dbService.getConnection();
//            statement = connection.prepareStatement("SELECT coupons, coupons_earned_ach, coupons_earned_cuts FROM achievements WHERE login = ?");
//            statement.setString(1, login);
//
//            ResultSet rs = statement.executeQuery();
//            while (rs.next()) {
//                responseNode.put("coupons", rs.getInt("coupons"));
//                responseNode.put("coupons_earned_ach", rs.getInt("coupons_earned_ach"));
//                responseNode.put("coupons_earned_cuts", rs.getInt("coupons_earned_cuts"));
//            }
//            dbService.closeConnection(connection);
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//
//        ArrayNode categoriesArray = getCouponChangeList(login);
//
//        responseNode.put("success", true);
//        responseNode.set("data", categoriesArray);
//        sendJsonResponse(ctx, HttpResponseStatus.OK, responseNode.toString());
//    }
//
//    public static ArrayNode getCouponChangeList(String login) {
//        ArrayNode categoriesArray = JsonNodeFactory.instance.arrayNode();
//        DatabaseService dbService = new DatabaseService(Constants.URL_DB, Constants.USER_DB, Constants.PASSWORD_DB);
//        PreparedStatement statement;
//        Connection connection;
//
//        try {
//            connection = dbService.getConnection();
//            statement = connection.prepareStatement("SELECT id, coupons, description, created, new_total FROM achievements_logs WHERE login = ? ORDER BY id desc LIMIT 100 ");
//            statement.setString(1, login);
//
//            ResultSet rs = statement.executeQuery();
//            while (rs.next()) {
//                ObjectNode categoryNode = JsonNodeFactory.instance.objectNode();
//                categoryNode.put("id", rs.getInt("id"));
//                categoryNode.put("coupons", rs.getInt("coupons"));
//                categoryNode.put("description", rs.getString("description"));
//                categoryNode.put("created", rs.getString("created"));
//                categoryNode.put("new_total", rs.getInt("new_total"));
//                categoriesArray.add(categoryNode);
//            }
//
//            dbService.closeConnection(connection);
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//
//        return categoriesArray;
//    }