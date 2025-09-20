package com.tvt.task_mgmt.mapper;

import com.tvt.task_mgmt.dto.request.PermissionRequest;
import com.tvt.task_mgmt.dto.response.PermissionResponse;
import com.tvt.task_mgmt.entity.Permission;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring" )
public interface PermissionMapper {
    @Mapping(target = "roles", ignore = true)
    Permission toPermission(PermissionRequest request);
    PermissionResponse toPermissionResponse(Permission permission);
}
