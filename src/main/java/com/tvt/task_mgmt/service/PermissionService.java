package com.tvt.task_mgmt.service;

import com.tvt.task_mgmt.dto.request.DeletePermissionRequest;
import com.tvt.task_mgmt.dto.request.PermissionRequest;
import com.tvt.task_mgmt.dto.response.MyApiResponse;
import com.tvt.task_mgmt.dto.response.PermissionResponse;

import java.util.List;

public interface PermissionService {
    MyApiResponse<PermissionResponse> create(PermissionRequest request);

    MyApiResponse<List<PermissionResponse>> getAll();

    MyApiResponse<?> delete(DeletePermissionRequest request);
}
