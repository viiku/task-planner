package com.vikku.taskplanner.taskservice.repository;

import com.vikku.taskplanner.taskservice.model.entities.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long> {
}
