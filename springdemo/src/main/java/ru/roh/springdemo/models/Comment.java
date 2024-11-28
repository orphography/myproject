package ru.roh.springdemo.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "Comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "task_id")
    private Long taskId;
    @Column(name = "user_id")
    private Long userId;
    @NotEmpty(message = "Comment should not be empty")
    private String text;
    private LocalDateTime createdAt;
}
