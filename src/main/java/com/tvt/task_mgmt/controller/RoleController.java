package com.tvt.task_mgmt.controller;

import com.tvt.task_mgmt.dto.request.RoleRequest;
import com.tvt.task_mgmt.service.RoleService;
import com.tvt.task_mgmt.dto.response.MyApiResponse;
import com.tvt.task_mgmt.dto.response.RoleResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping("/roles")
public class RoleController {
    RoleService roleService;

    @GetMapping
    MyApiResponse<List<RoleResponse>> getAll() {
        return roleService.getAll();
    }

    @PostMapping
    MyApiResponse<RoleResponse> create(@RequestBody RoleRequest request) {
        return roleService.create(request);
    }

    @DeleteMapping("/{role}")
    MyApiResponse<?> delete(@PathVariable String role) {
        return roleService.delete(role);
    }
}
