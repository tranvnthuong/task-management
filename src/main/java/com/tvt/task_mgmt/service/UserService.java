package com.tvt.task_mgmt.service;

import com.tvt.task_mgmt.dto.request.ChangeEmailRequest;
import com.tvt.task_mgmt.dto.request.ChangePasswordRequest;
import com.tvt.task_mgmt.dto.request.ProfileDTO;
import com.tvt.task_mgmt.dto.request.UserRequest;
import com.tvt.task_mgmt.dto.response.MyApiResponse;
import com.tvt.task_mgmt.dto.response.UserResponse;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public interface UserService {
    MyApiResponse<UserResponse> getSelfInfo();

    MyApiResponse<UserResponse> createUser(UserRequest request);

    MyApiResponse<UserResponse> userProfileUpdate(String userId, ProfileDTO request);

    MyApiResponse<Object> deleteUser(String userId);

    @PreAuthorize("hasRole('ADMIN')")
    MyApiResponse<List<UserResponse>> getAllUserInfo();

    @PreAuthorize("hasRole('ADMIN')")
    MyApiResponse<UserResponse> getUserById(String userId);

    MyApiResponse<Object> changePassword(String userId, ChangePasswordRequest request);
    MyApiResponse<Object> changeEmail(String userId, ChangeEmailRequest request);
}
