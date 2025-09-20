package com.tvt.task_mgmt.controller;

import com.tvt.task_mgmt.dto.request.TaskRequest;
import com.tvt.task_mgmt.dto.response.MyApiResponse;
import com.tvt.task_mgmt.dto.response.TaskResponse;
import com.tvt.task_mgmt.service.TaskService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TaskController {
    TaskService taskService;

    @GetMapping("/get-all")
    MyApiResponse<Page<TaskResponse>> getAllTask(@RequestParam(required = false) Pageable pageable) {
        return taskService.getAllTask(pageable);
    }

    @GetMapping("/my-task/{userId}")
    MyApiResponse<Page<TaskResponse>> getMyTask(@PathVariable String userId, @RequestParam(required = false) Pageable pageable) {
        return taskService.getMyTask(userId, pageable);
    }

    @PostMapping("/create")
    MyApiResponse<TaskResponse> createTask(@Valid @RequestBody TaskRequest request) {
        return taskService.createTask(request);
    }

    @PutMapping("/edit/{taskId}")
    MyApiResponse<TaskResponse> updateTask(@PathVariable String taskId, @Valid @RequestBody TaskRequest request) {
        return taskService.updateTask(taskId, request);
    }

    @DeleteMapping("/delete/{taskId}")
    MyApiResponse<Object> deleteTask(@PathVariable String taskId) {
        return taskService.deleteTask(taskId);
    }
}
