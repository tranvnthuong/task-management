package com.tvt.task_mgmt.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tvt.task_mgmt.exception.ErrorCode;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MyApiResponse<T> {
    boolean success;
    String message;
    Integer error_code;
    T data;

    public static <T> MyApiResponse<T> of(ErrorCode errorCode, T data) {
        boolean success = errorCode.isSuccess();
        return MyApiResponse.<T>builder()
                .success(success)
                .message(errorCode.getMessage())
                .error_code(success ? null : errorCode.getCode())
                .data(data)
                .build();
    }

    public static MyApiResponse<Object> of(ErrorCode errorCode) {
        boolean success = errorCode.isSuccess();
        return MyApiResponse.builder()
                .success(success)
                .message(errorCode.getMessage())
                .error_code(success ? null : errorCode.getCode())
                .build();
    }
}
