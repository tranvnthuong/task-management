package com.tvt.task_mgmt.dto.response;

import com.tvt.task_mgmt.dto.request.ProfileDTO;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    String id;
    String username;
    String email;
    Set<RoleResponse> roles;
    ProfileDTO profile;
}
