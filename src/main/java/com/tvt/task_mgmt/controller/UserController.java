package com.tvt.task_mgmt.controller;

import com.tvt.task_mgmt.dto.request.ChangeEmailRequest;
import com.tvt.task_mgmt.dto.request.ChangePasswordRequest;
import com.tvt.task_mgmt.dto.request.ProfileDTO;
import com.tvt.task_mgmt.dto.request.UserRequest;
import com.tvt.task_mgmt.service.UserService;
import com.tvt.task_mgmt.dto.response.MyApiResponse;
import com.tvt.task_mgmt.dto.response.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping("/users")
public class UserController {
    UserService userService;

    @Operation(summary = "Lấy user theo security context", description = "Trả về chi tiết User nếu đã xác minh.")
    @ApiResponses({
            @ApiResponse(responseCode = "404", description = "Không tìm thấy user"),
            @ApiResponse(responseCode = "403", description = "user chưa xác minh"),

            @ApiResponse(responseCode = "200", description = "Thành công",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MyApiResponse.class))
            ),
    })
    @GetMapping
    MyApiResponse<UserResponse> getSelfInfo() {
        return userService.getSelfInfo();
    }

    @Operation(summary = "Tạo user mới", description = "Trả về thông tin user kèm theo vai trò quyền hạn.")
    @ApiResponses({
            @ApiResponse(responseCode = "403", description = "User đã tồn tại"),

            @ApiResponse(responseCode = "200", description = "Thành công",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MyApiResponse.class))
            )
    })
    @PostMapping("/register")
    MyApiResponse<UserResponse> createUser(@Valid @RequestBody UserRequest request) {
        return userService.createUser(request);
    }

    @PutMapping("/profile-update/{userId}")
    MyApiResponse<UserResponse> updateUser(@PathVariable String userId,
                                           @Valid @RequestBody ProfileDTO request) {
        return userService.userProfileUpdate(userId, request);
    }

    @DeleteMapping("/delete/{userId}")
    MyApiResponse<Object> deleteUser(@PathVariable String userId) {
        return userService.deleteUser(userId);
    }

    @PostMapping("/change-password/{userId}")
    MyApiResponse<Object> changePw(@PathVariable String userId, @Valid @RequestBody ChangePasswordRequest request) {
        return userService.changePassword(userId, request);
    }

    @PostMapping("/change-email/{userId}")
    MyApiResponse<Object> changeEmail(@PathVariable String userId, @Valid @RequestBody ChangeEmailRequest request) {
        return userService.changeEmail(userId, request);
    }

    @GetMapping("/get-all")
    MyApiResponse<List<UserResponse>> getAllUserInfo() {
        return userService.getAllUserInfo();
    }

    @GetMapping("/get-by-id/{userId}")
    MyApiResponse<UserResponse> getUser(@PathVariable String userId) {
        return userService.getUserById(userId);
    }
}
