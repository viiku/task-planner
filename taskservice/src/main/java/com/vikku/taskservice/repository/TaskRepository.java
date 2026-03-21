package com.vikku.taskservice.repository;

import com.vikku.taskservice.model.entities.TaskEntity;
import com.vikku.taskservice.model.enums.TaskStatus;
import com.vikku.taskservice.model.enums.TaskType;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long> {

    boolean existsByTaskId(String taskId);

    TaskEntity findByTaskId(String taskId);

    @Transactional
    void deleteByTaskId(String taskId);

    List<TaskEntity> findAllByTaskTypeAndTaskStatus(TaskType type, TaskStatus status);

    List<TaskEntity> findAllTaskByTaskType(TaskType type);
}
