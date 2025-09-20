package com.tvt.task_mgmt.service;

import com.tvt.task_mgmt.dto.request.RoleRequest;
import com.tvt.task_mgmt.dto.response.MyApiResponse;
import com.tvt.task_mgmt.dto.response.RoleResponse;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public interface RoleService {
    MyApiResponse<RoleResponse> create(RoleRequest request);

    @PreAuthorize("hasRole('ADMIN')")
    MyApiResponse<List<RoleResponse>> getAll();

    MyApiResponse<Object> delete(String role);
}
