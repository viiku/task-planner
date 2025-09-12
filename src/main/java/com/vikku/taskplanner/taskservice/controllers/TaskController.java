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
    public ResponseEntity<TaskResponse> createTask(@RequestBody TaskCreateRequest taskRequest) {
        return ResponseEntity.ok(taskService.createTask(taskRequest));
    }

    @GetMapping("/{taskId}")
    @Operation(summary = "Get task using taskId")
    public ResponseEntity<TaskDto> getTask(@PathVariable String taskId) {
        return ResponseEntity.ok(taskService.getTask(taskId));
    }

    @PutMapping("/{taskId}")
    @Operation(summary = "Update task using taskId")
    public ResponseEntity<TaskResponse> updateTask(
            @PathVariable String taskId,
            @RequestBody TaskUpdateRequest taskRequest) {
        return ResponseEntity.ok(taskService.updateTask(taskId, taskRequest));
    }

    @DeleteMapping("/{taskId}")
    @Operation(summary = "Delete task using taskId")
    public void deleteTask(@PathVariable String taskId) {
        taskService.deleteTask(taskId);
    }

    @GetMapping("/type")
    @Operation(summary = "Get list of task by type")
    public ResponseEntity<List<TaskDto>> getTaskByType(@RequestParam TaskType type) {
        return ResponseEntity.ok(taskService.getTaskByType(type));
    }

    @GetMapping
    @Operation(summary = "Get list of task by type and status")
    public ResponseEntity<List<TaskDto>> getAllTasks(
            @RequestParam(required = false) TaskType type,
            @RequestParam(required = false) TaskStatus status) {
        return ResponseEntity.ok(taskService.getAllTasksByTypeAndStatus(type, status));
    }

}
