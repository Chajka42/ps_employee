package ru.unisafe.psemployee.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class CouponsInfoResponse {
    private boolean success;
    private int coupons;
    private int couponsEarnedAch;
    private int couponsEarnedCuts;
    private List<CouponChange> data;

    @Data
    @Builder
    public static class CouponChange {
        private int id;
        private int coupons;
        private String description;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy HH:mm:ss", locale = "ru")
        private LocalDateTime created;
        private int newTotal;
    }
}
