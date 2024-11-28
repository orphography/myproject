package ru.roh.springdemo.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.roh.springdemo.DTO.TaskDTO;
import ru.roh.springdemo.services.TaskService;
import ru.roh.springdemo.utils.NotCreatedException;
import ru.roh.springdemo.utils.NotUpdateException;
import ru.roh.springdemo.utils.Priority;
import ru.roh.springdemo.utils.Status;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@Tag(name = "Задачи", description = "Управление задачами")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @GetMapping
    @Operation(summary = "Получить все задачи", description = "Возвращает список всех задач")
    public ResponseEntity<List<TaskDTO>> getAllTasks() {
        return new ResponseEntity<>(taskService.getAllTasks(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить задачу по ID", description = "Возвращает задачу по ID")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable Long id) {
        TaskDTO taskDTO = taskService.getTaskById(id);
        return new ResponseEntity<>(taskDTO, HttpStatus.OK);
    }

    @PostMapping
    @Operation(summary = "Создать новую задачу", description = "Добавляет новую задачу в систему")
    public ResponseEntity<TaskDTO> createTask(@RequestBody @Valid TaskDTO taskDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new NotCreatedException(buildErrorMessage(bindingResult));
        }
        TaskDTO actualTask = taskService.createTask(taskDTO);
        return new ResponseEntity<>(actualTask, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить задачу", description = "Обновляет данные задачи")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable Long id, @Valid @RequestBody TaskDTO taskDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new NotUpdateException(buildErrorMessage(bindingResult));
        }
        TaskDTO updatedTask = taskService.updateTask(id, taskDTO);
        return new ResponseEntity<>(updatedTask, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить задачу", description = "Удаляет задачу из системы")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/status/{status}")
    @Operation(summary = "Поиск задач по статусу", description = "Возвращает все задачи с указанным статусом")
    public ResponseEntity<List<TaskDTO>> getTasksByStatus(@PathVariable Status status) {
        List<TaskDTO> listTaskDTO = taskService.getTasksByStatus(status);
        return new ResponseEntity<>(listTaskDTO, HttpStatus.OK);
    }
    @GetMapping("/priority/{priority}")
    @Operation(summary = "Поиск задач по приоритету", description = "Возвращает все задачи с указанным приоритетом")
    public ResponseEntity<List<TaskDTO>> getTasksByPriority(@PathVariable Priority priority) {
        List<TaskDTO> listTaskDTO = taskService.getTasksByPriority(priority);
        return new ResponseEntity<>(listTaskDTO, HttpStatus.OK);
    }

    @GetMapping("/assignee/{userId}")
    @Operation(summary = "Поиск задач по пользователю", description = "Возвращает все задачи связанные с указанным пользователем")
    public ResponseEntity<List<TaskDTO>> getTasksByAssignee(@PathVariable Long userId) {
        List<TaskDTO> listTaskDTO = taskService.getTasksByAssignee(userId);
        return new ResponseEntity<>(listTaskDTO, HttpStatus.OK);
    }

    @GetMapping("/project/{projectId}")
    @Operation(summary = "Поиск задач по проекту", description = "Возвращает все задачи связанные с указанным проектом")
    public ResponseEntity<List<TaskDTO>> getTasksByProject(@PathVariable Long projectId) {
        List<TaskDTO> taskDTOList = taskService.getTasksByProject(projectId);
        return new ResponseEntity<>(taskDTOList, HttpStatus.OK);
    }
    @GetMapping("/search/{title}")
    @Operation(summary = "Поиск задач по ключевому слову", description = "Возвращает все задачи в названии которых есть ключевое слово")
    public ResponseEntity<List<TaskDTO>> getTasksByTitle(@PathVariable String title) {
        List<TaskDTO> taskDTOList = taskService.getTasksByTitleContaining(title);
        return new ResponseEntity<>(taskDTOList, HttpStatus.OK);
    }
    @PutMapping("/{taskId}/status")
    @Operation(summary = "Изменить статус задачи", description = "Изменяет статус у указанной задачи")
    public ResponseEntity<TaskDTO> updateTaskStatus(@PathVariable Long taskId, @RequestBody Status newStatus) {
        TaskDTO updatedTask = taskService.updateTaskStatus(taskId, newStatus);
        return new ResponseEntity<>(updatedTask, HttpStatus.OK);
    }

    private String buildErrorMessage(BindingResult bindingResult) {
        StringBuilder errMsg = new StringBuilder();
        List<FieldError> errors = bindingResult.getFieldErrors();
        for (FieldError error : errors) {
            errMsg.append(String.format("%s: %s;", error.getField(), error.getDefaultMessage()));
        }
        return errMsg.toString();
    }
}
