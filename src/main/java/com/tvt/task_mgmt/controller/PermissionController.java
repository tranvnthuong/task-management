package com.tvt.task_mgmt.controller;

import com.tvt.task_mgmt.dto.request.DeletePermissionRequest;
import com.tvt.task_mgmt.dto.request.PermissionRequest;
import com.tvt.task_mgmt.service.PermissionService;
import com.tvt.task_mgmt.dto.response.MyApiResponse;
import com.tvt.task_mgmt.dto.response.PermissionResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping("/permissions")
public class PermissionController {
    PermissionService permissionService;

    @GetMapping
    MyApiResponse<List<PermissionResponse>> getAll() {
        return permissionService.getAll();
    }

    @PostMapping
    MyApiResponse<PermissionResponse> create(@RequestBody PermissionRequest request) {
        return permissionService.create(request);
    }

    @DeleteMapping
    MyApiResponse<?> delete(@RequestBody DeletePermissionRequest request) {
        return permissionService.delete(request);
    }
}
