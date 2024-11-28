package ru.roh.springdemo.DTO;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.annotations.Type;
import ru.roh.springdemo.utils.TaskChangeDetails;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class TaskHistoryDTO {
    private Long taskId;
    private Long changedBy;
    private LocalDateTime changeDate;
    private List<TaskChangeDetails> changeDetails;
}
