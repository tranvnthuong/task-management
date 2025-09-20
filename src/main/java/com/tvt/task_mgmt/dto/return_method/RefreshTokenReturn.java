package com.tvt.task_mgmt.dto.return_method;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RefreshTokenReturn {
    String token;
    int expiry; //Day
    String accessToken;
    Date accessTokenExpires;
}
