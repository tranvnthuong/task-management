package com.tvt.task_mgmt.service.impl;

import com.tvt.task_mgmt.dto.request.DeletePermissionRequest;
import com.tvt.task_mgmt.dto.request.PermissionRequest;
import com.tvt.task_mgmt.dto.response.MyApiResponse;
import com.tvt.task_mgmt.dto.response.PermissionResponse;
import com.tvt.task_mgmt.exception.ErrorCode;
import com.tvt.task_mgmt.entity.Permission;
import com.tvt.task_mgmt.mapper.PermissionMapper;
import com.tvt.task_mgmt.repository.PermissionRepository;
import com.tvt.task_mgmt.service.PermissionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class PermissionServiceImpl implements PermissionService {
    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;

    @Override
    public MyApiResponse<PermissionResponse> create(PermissionRequest request) {
        Permission permission = permissionMapper.toPermission(request);
        permission = permissionRepository.save(permission);
        return MyApiResponse.of(ErrorCode.SUCCESS, permissionMapper.toPermissionResponse(permission));
    }

    @Override
    public MyApiResponse<List<PermissionResponse>> getAll() {
        List<Permission> permissions = permissionRepository.findAll();
        return MyApiResponse.of(
                ErrorCode.SUCCESS,
                permissions.stream().map(permissionMapper::toPermissionResponse).toList()
        );
    }

    @Override
    public MyApiResponse<?> delete(DeletePermissionRequest request) {
        permissionRepository.deleteAllById(request.getPermissions());
        return MyApiResponse.of(ErrorCode.SUCCESS);
    }
}
