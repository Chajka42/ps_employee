package ru.unisafe.psemployee.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.unisafe.psemployee.dto.StoreItemDto;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class VisitStationRequest {
    @NotBlank(message = "Логин станции не может быть пустым")
    private String login;
    @NotBlank(message = "Код станции не может быть пустым")
    private String code;
    @NotBlank(message = "Адрес не может быть пустым")
    private String address;
    private String comment;
    @Min(value = 1, message = "ID партнера должен быть больше или равен 1")
    private int partnerId;
    @NotBlank(message = "Название партнера обязательно для заполнения")
    private String partnerName;
    @NotBlank(message = "Токен не может быть пустым")
    private String token;
    private boolean problemSolved;
    private String problemDescription;
    private boolean defectGathering;
    @DecimalMin(value = "-90.0", message = "Широта должна быть в пределах от -90.0 до 90.0")
    @DecimalMax(value = "90.0", message = "Широта должна быть в пределах от -90.0 до 90.0")
    private double lat;
    @DecimalMin(value = "-180.0", message = "Долгота должна быть в пределах от -180.0 до 180.0")
    @DecimalMax(value = "180.0", message = "Долгота должна быть в пределах от -180.0 до 180.0")
    private double lon;
    @Min(value = 0, message = "Дистанция должна быть больше или равна 0")
    private int distance;
    private String problemOrigin;
    private List<StoreItemDto> dataList;
}
