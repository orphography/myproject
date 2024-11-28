package ru.roh.springdemo.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProjectDTO {
    @NotBlank(message = "ProjectName should not be empty")
    private String name;
    @NotBlank(message = "DescriptionProject should not be empty")
    private String description;
}
