package com.tvt.task_mgmt.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tvt.task_mgmt.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthResponse {
    @Schema(hidden = true)
    User user;
    boolean authenticated;
    String fullName;
    String tokenType;
    String accessToken;
    Date tokenExpiresAt;
}
