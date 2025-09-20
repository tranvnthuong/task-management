package com.tvt.task_mgmt.service.impl;

import com.tvt.task_mgmt.dto.request.RoleRequest;
import com.tvt.task_mgmt.service.RoleService;
import com.tvt.task_mgmt.dto.response.MyApiResponse;
import com.tvt.task_mgmt.exception.ErrorCode;
import com.tvt.task_mgmt.dto.response.RoleResponse;
import com.tvt.task_mgmt.entity.Permission;
import com.tvt.task_mgmt.entity.Role;
import com.tvt.task_mgmt.mapper.RoleMapper;
import com.tvt.task_mgmt.repository.PermissionRepository;
import com.tvt.task_mgmt.repository.RoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class RoleServiceImpl implements RoleService {
    RoleRepository roleRepository;
    PermissionRepository permissionRepository;
    RoleMapper roleMapper;

    @Override
    public MyApiResponse<RoleResponse> create(RoleRequest request) {
        Role role = roleMapper.toRole(request);

        List<Permission> permissions = permissionRepository.findAllById(request.getPermissions());
        role.setPermissions(new HashSet<>(permissions));

        Role saveRole = roleRepository.save(role);
        return MyApiResponse.of(ErrorCode.SUCCESS, roleMapper.toRoleResponse(saveRole));
    }

    @Override
    public MyApiResponse<List<RoleResponse>> getAll() {
        List<Role> roles = roleRepository.findAll();
        return MyApiResponse.of(
                ErrorCode.SUCCESS,
                roles.stream().map(roleMapper::toRoleResponse).toList()
        );
    }

    @Override
    public MyApiResponse<Object> delete(String role) {
        roleRepository.deleteById(role);
        return MyApiResponse.of(ErrorCode.SUCCESS);
    }
}
