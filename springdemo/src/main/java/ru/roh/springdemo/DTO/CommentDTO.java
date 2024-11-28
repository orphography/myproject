package ru.roh.springdemo.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CommentDTO {
    private Long taskId;
    private Long userId;
    @NotBlank(message = "Comment should not be empty")
    private String text;
}
