package ru.roh.springdemo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.roh.springdemo.models.Project;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByNameContainingIgnoreCase(String name);

    List<Project> findByStartDateAfter(LocalDateTime startDate);
}
