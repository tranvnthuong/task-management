package com.tvt.task_mgmt.repository;

import com.tvt.task_mgmt.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, String> {
    Page<Task> findAllByAssignedTo_Id(String assignedUserId, Pageable pageable);
    List<Task> findAllByAssignedTo_Id(String assignedUserId);
}
