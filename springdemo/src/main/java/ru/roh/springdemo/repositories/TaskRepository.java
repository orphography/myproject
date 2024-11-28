package ru.roh.springdemo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.roh.springdemo.models.Task;
import ru.roh.springdemo.utils.Priority;
import ru.roh.springdemo.utils.Status;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByStatus(Status status);

    List<Task> findByPriority(Priority priority);

    List<Task> findByAssigneeId(Long userId);

    List<Task> findByProjectId(Long projectId);

    List<Task> findByTitleContainingIgnoreCase(String title);
}
