package com.tvt.task_mgmt.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRequest {
    @Size(min = 4, message = "MINIMUM_USERNAME")
    @Size(max = 20, message = "MAXIMUM_USERNAME")
    String username;

    @Size(min = 6, message = "MINIMUM_PASSWORD")
    @Size(max = 50, message = "MAXIMUM_PASSWORD")
    String password;

    @Size(max = 50, message = "MAXIMUM_EMAIL")
    @Email(message = "INVALID_EMAIL")
    String email;

    @Valid
    ProfileDTO profile;
}
