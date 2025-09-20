package com.tvt.task_mgmt.service;

import com.tvt.task_mgmt.dto.request.AuthRequest;
import com.tvt.task_mgmt.dto.request.IntrospectRequest;
import com.tvt.task_mgmt.dto.request.LogoutRequest;
import com.tvt.task_mgmt.dto.response.MyApiResponse;
import com.tvt.task_mgmt.dto.response.AuthResponse;
import com.tvt.task_mgmt.dto.response.IntrospectResponse;
import com.tvt.task_mgmt.entity.User;

public interface AuthService {
    //log-in
    AuthResponse authenticate(AuthRequest request);

    MyApiResponse<IntrospectResponse> introspect(IntrospectRequest request);

    void logout(LogoutRequest request);

    String generateToken(User user);
}
