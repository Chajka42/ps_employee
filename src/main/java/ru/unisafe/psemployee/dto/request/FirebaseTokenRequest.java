package ru.unisafe.psemployee.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FirebaseTokenRequest {

    @Parameter(description = "ID пользователя", example = "1")
    private Integer id;

    @Parameter(description = "Firebase токен. Может быть 'none'", example = "none")
    @JsonProperty("firebase_token")
    private String firebaseToken;
}
