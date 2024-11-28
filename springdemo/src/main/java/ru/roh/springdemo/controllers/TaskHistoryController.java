package ru.roh.springdemo.controllers;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.roh.springdemo.DTO.TaskHistoryDTO;
import ru.roh.springdemo.services.TaskHistoryService;

import java.util.List;

@RestController
@RequestMapping("/api/history/{id}")
public class TaskHistoryController {

    @Autowired
    private TaskHistoryService taskHistoryService;

    @GetMapping
    @Operation(summary = "Получить историю изменения задачи", description = "Возвращает список изменений задачи")
    public ResponseEntity<List<TaskHistoryDTO>> getTaskHistory(@PathVariable Long id) {
        return new ResponseEntity<>(taskHistoryService.getHistoryByTaskId(id), HttpStatus.OK);
    }
}
