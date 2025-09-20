package com.tvt.task_mgmt.service;

import com.tvt.task_mgmt.dto.request.TaskRequest;
import com.tvt.task_mgmt.dto.response.MyApiResponse;
import com.tvt.task_mgmt.dto.response.TaskResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TaskService {
    MyApiResponse<Page<TaskResponse>> getAllTask(Pageable pageable);

    MyApiResponse<Page<TaskResponse>> getMyTask(String assignedUserId, Pageable pageable);

    MyApiResponse<TaskResponse> createTask(TaskRequest request);

    MyApiResponse<TaskResponse> updateTask(String taskId, TaskRequest request);

    MyApiResponse<Object> deleteTask(String taskId);
}
