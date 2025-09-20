package com.tvt.task_mgmt.mapper;

import com.tvt.task_mgmt.dto.request.TaskRequest;
import com.tvt.task_mgmt.dto.response.TaskResponse;
import com.tvt.task_mgmt.entity.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    Task toTask(TaskRequest taskRequest);

    TaskResponse toTaskResponse(Task task);
    List<TaskResponse> toTaskResponseList(List<Task> tasks);

    @Mapping(target = "assignedTo", ignore = true)
    void updateTask(@MappingTarget Task task, TaskRequest request);
}
