package com.tvt.task_mgmt.dto.request;

import com.tvt.task_mgmt.dto.validator.DobConstraint;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProfileDTO {

    @NotBlank
    String avatar;

    @NotBlank
    String fullName;

    @DobConstraint(min = 6, message = "INVALID_DOB")
    LocalDate dayOfBirth;
}
