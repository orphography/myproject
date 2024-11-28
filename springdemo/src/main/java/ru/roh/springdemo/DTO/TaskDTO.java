package ru.roh.springdemo.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.roh.springdemo.utils.Priority;
import ru.roh.springdemo.utils.Status;

@Data
@Schema(description = "Модель данных задачи")
public class TaskDTO {
    @NotBlank(message = "Title should not be empty")
    @Schema(description = "Задача", example = "внедрить lombok")
    private String title;
    @NotBlank(message = "Description should not be empty")
    @Schema(description = "Описание задачи", example = "подключить зависимость lombok")
    private String description;
    @NotNull(message = "Priority should not be null")
    @Schema(description = "Приоритет задачи", example = "HIGH")
    private Priority priority;
    @NotNull(message = "Status should not be null")
    @Schema(description = "статус задачи", example = "IN_PROGRESS")
    private Status status;
}
