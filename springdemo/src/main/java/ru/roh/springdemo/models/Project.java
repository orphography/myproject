package ru.roh.springdemo.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "Projects")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotEmpty(message = "ProjectName should not be empty")
    private String name;
    @NotEmpty(message = "DescriptionProject should not be empty")
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
