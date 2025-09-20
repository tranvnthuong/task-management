package com.tvt.task_mgmt.service.impl;

import com.tvt.task_mgmt.dto.request.TaskRequest;
import com.tvt.task_mgmt.dto.response.MyApiResponse;
import com.tvt.task_mgmt.dto.response.TaskResponse;
import com.tvt.task_mgmt.entity.Task;
import com.tvt.task_mgmt.entity.User;
import com.tvt.task_mgmt.exception.AppException;
import com.tvt.task_mgmt.exception.ErrorCode;
import com.tvt.task_mgmt.mapper.TaskMapper;
import com.tvt.task_mgmt.repository.TaskRepository;
import com.tvt.task_mgmt.repository.UserRepository;
import com.tvt.task_mgmt.service.TaskService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class TaskServiceImpl implements TaskService {
    TaskRepository taskRepository;
    TaskMapper taskMapper;
    UserRepository userRepository;

    @Override
    public MyApiResponse<Page<TaskResponse>> getAllTask(Pageable pageable) {
        Page<Task> taskPage = taskRepository.findAll(pageable);
        return MyApiResponse.of(ErrorCode.SUCCESS, taskPage.map(taskMapper::toTaskResponse));
    }

    @Override
    public MyApiResponse<Page<TaskResponse>> getMyTask(String assignedUserId, Pageable pageable) {
        Page<Task> taskPage = taskRepository.findAllByAssignedTo_Id(assignedUserId, pageable);
        return MyApiResponse.of(ErrorCode.SUCCESS, taskPage.map(taskMapper::toTaskResponse));
    }

    @Override
    public MyApiResponse<TaskResponse> createTask(TaskRequest request) {
        Task task = taskMapper.toTask(request);

        if (request.getAssignedUserId() != null) {
            User user = userRepository.findById(request.getAssignedUserId())
                    .orElseThrow(() -> new AppException(ErrorCode.USER_DOES_NOT_EXIST));

            List<Task> userTasks = taskRepository.findAllByAssignedTo_Id(request.getAssignedUserId());
            userTasks.add(task);
            user.setTasks(userTasks);

            task.setAssignedTo(user);

            userRepository.save(user);
        }

        task = taskRepository.save(task);
        return MyApiResponse.of(ErrorCode.SUCCESS, taskMapper.toTaskResponse(task));
    }

    @Override
    public MyApiResponse<TaskResponse> updateTask(String taskId, TaskRequest request) {
        var task = taskRepository.findById(taskId)
                .orElseThrow(() -> new AppException(ErrorCode.TASK_NOT_FOUND));

        taskMapper.updateTask(task, request);

        if (request.getAssignedUserId() != null
                && !task.getAssignedTo().getId().equals(request.getAssignedUserId())
        ) {
            User user = userRepository.findById(request.getAssignedUserId())
                    .orElseThrow(() -> new AppException(ErrorCode.USER_DOES_NOT_EXIST));

            List<Task> userTasks = taskRepository.findAllByAssignedTo_Id(request.getAssignedUserId());
            userTasks.add(task);
            user.setTasks(userTasks);

            task.setAssignedTo(user);

            userRepository.save(user);
        }

        task = taskRepository.save(task);
        return MyApiResponse.of(ErrorCode.SUCCESS, taskMapper.toTaskResponse(task));
    }

    @Override
    public MyApiResponse<Object> deleteTask(String taskId) {
        taskRepository.deleteById(taskId);
        return MyApiResponse.of(ErrorCode.SUCCESS);
    }
}
