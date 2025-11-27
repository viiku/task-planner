package com.vikku.taskservice.taskservice.repository;

import com.vikku.taskservice.taskservice.model.entities.TaskEntity;
import com.vikku.taskservice.taskservice.model.enums.TaskStatus;
import com.vikku.taskservice.taskservice.model.enums.TaskType;
import jakarta.transaction.Transactional;
import org.hibernate.sql.model.ast.builder.TableUpdateBuilderSkipped;
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
