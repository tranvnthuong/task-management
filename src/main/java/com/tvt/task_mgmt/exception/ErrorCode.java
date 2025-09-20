package com.tvt.task_mgmt.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ErrorCode {
    UNCAUGHT_EXCEPTION(9050, "uncaught_exception", HttpStatus.INTERNAL_SERVER_ERROR),
    BAD_REQUEST(9000, "invalid_request", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED(9001, "unauthorized", HttpStatus.UNAUTHORIZED),
    FORBIDDEN(9003, "no_access", HttpStatus.FORBIDDEN),
    SUCCESS(1000, "success", HttpStatus.OK),
    AUTHENTICATED(1000, "login_successful", HttpStatus.OK),
    LOGOUT(1000, "logout_successful", HttpStatus.OK),
    USER_CREATED(1000, "new_user_created", HttpStatus.OK),
    USER_UPDATED(1000, "user_update_successful", HttpStatus.OK),
    USER_DELETED(1000, "user_has_been_deleted", HttpStatus.OK),
    WRONG_PASSWORD(1001, "wrong_password", HttpStatus.UNAUTHORIZED),
    VERIFICATION_FAILED(1002, "verification_failed", HttpStatus.UNAUTHORIZED),
    USER_ALREADY_EXIST(1003, "user_already_exist", HttpStatus.FORBIDDEN),
    USER_DOES_NOT_EXIST(1004, "user_does_not_exist", HttpStatus.NOT_FOUND),
    MINIMUM_USERNAME(1005, "Username must be at least {min} characters", HttpStatus.BAD_REQUEST),
    MAXIMUM_USERNAME(1006, "Username contains up to {max} characters", HttpStatus.BAD_REQUEST),
    MINIMUM_PASSWORD(1007, "Password must be at least {min} characters", HttpStatus.BAD_REQUEST),
    MAXIMUM_PASSWORD(1008, "Password contains up to {max} characters", HttpStatus.BAD_REQUEST),
    MAXIMUM_EMAIL(1009, "Email contains up to {max} characters", HttpStatus.BAD_REQUEST),
    INVALIDATED_TOKEN(1010, "invalidated_token", HttpStatus.UNAUTHORIZED),
    TASK_NOT_FOUND(1011, "card_not_found", HttpStatus.NOT_FOUND)
    ;

    int code;
    String message;
    HttpStatusCode statusCode;

    public boolean isSuccess() {
        return this.code == 1000;
    }
}

