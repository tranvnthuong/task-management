package com.tvt.task_mgmt.mapper;

import com.tvt.task_mgmt.dto.request.RoleRequest;
import com.tvt.task_mgmt.dto.response.RoleResponse;
import com.tvt.task_mgmt.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "users", ignore = true)
    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);

    RoleResponse toRoleResponse(Role role);
}
