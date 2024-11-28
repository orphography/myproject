package ru.roh.springdemo.models;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Type;
import ru.roh.springdemo.utils.TaskChangeDetails;

import java.time.LocalDateTime;
import java.util.List;
@Entity
@Data
@Table(name = "Task_history")
public class TaskHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long taskId;
    private Long changedBy;
    private LocalDateTime changeDate;
    @Type(JsonBinaryType.class)
    @Column(columnDefinition = "jsonb")
    private List<TaskChangeDetails> changeDetails; // Список изменений
}


