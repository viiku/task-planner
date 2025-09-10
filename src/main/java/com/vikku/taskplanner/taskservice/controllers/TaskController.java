package com.vikku.taskplanner.taskservice.controllers;

import com.vikku.taskplanner.taskservice.model.dtos.TaskDto;
import com.vikku.taskplanner.taskservice.model.dtos.request.TaskCreateRequest;
import com.vikku.taskplanner.taskservice.model.dtos.request.TaskUpdateRequest;
import com.vikku.taskplanner.taskservice.model.dtos.response.TaskResponse;
import com.vikku.taskplanner.taskservice.model.enums.TaskStatus;
import com.vikku.taskplanner.taskservice.model.enums.TaskType;
import com.vikku.taskplanner.taskservice.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller class for creating, updating, deleting tasks
 */
@RestController
@RequestMapping("/api/v1/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    @Operation(summary = "Create a new task")
    public ResponseEntity<TaskDto> createTask(@RequestBody TaskCreateRequest taskRequest) {
        return ResponseEntity.ok(taskService.createTask(taskRequest));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get task")
    public ResponseEntity<TaskDto> getTask(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getTask(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update task")
    public ResponseEntity<TaskResponse> updateTask(@RequestBody TaskUpdateRequest taskRequest) {
        return ResponseEntity.ok(taskService.updateTask(taskRequest));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete task")
    public void deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
    }

    @GetMapping
    @Operation(summary = "Get all task of all category")
    public ResponseEntity<List<TaskDto>> getAllTasks(
            @RequestParam(required = false) TaskType type,
            @RequestParam(required = false) TaskStatus status) {
        return ResponseEntity.ok(taskService.getAllTasksByTypeAndStatus(type, status));
    }

    @GetMapping("/type")
    @Operation(summary = "Get task by type")
    public ResponseEntity<List<TaskDto>> getTaskByType(@RequestParam TaskType type) {
        return ResponseEntity.ok(taskService.getTaskByType(type));
    }
}
