package ru.roh.springdemo.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import ru.roh.springdemo.utils.Priority;
import ru.roh.springdemo.utils.Status;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "Tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotEmpty(message = "Title should not be empty")
    private String title;
    @NotEmpty(message = "Description should not be empty")
    private String description;
    @Enumerated(EnumType.STRING)
    private Priority priority;
    @Enumerated(EnumType.STRING)
    private Status status;
    private LocalDateTime startDate;
    private LocalDateTime dueDate;
    private Long assigneeId;
    private Long projectId;
}
