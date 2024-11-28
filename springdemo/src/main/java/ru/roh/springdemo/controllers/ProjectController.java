package ru.roh.springdemo.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.roh.springdemo.DTO.ProjectDTO;
import ru.roh.springdemo.services.ProjectService;
import ru.roh.springdemo.utils.NotCreatedException;
import ru.roh.springdemo.utils.NotUpdateException;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @GetMapping
    public ResponseEntity<List<ProjectDTO>> getAllProjects() {
        return new ResponseEntity<>(projectService.getAllProjects(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectDTO> getProjectById(@PathVariable Long id) {
        ProjectDTO projectDTO = projectService.getProjectById(id);
        return new ResponseEntity<>(projectDTO, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ProjectDTO> createProject(@RequestBody @Valid ProjectDTO projectDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new NotCreatedException(buildErrorMessage(bindingResult));
        }
        ProjectDTO actualProject = projectService.createProject(projectDTO);
        return new ResponseEntity<>(actualProject, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectDTO> updateProject(@PathVariable Long id, @Valid @RequestBody ProjectDTO projectDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new NotUpdateException(buildErrorMessage(bindingResult));
        }
        ProjectDTO updatedProject = projectService.updateProject(id, projectDTO);
        return new ResponseEntity<>(updatedProject, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProjectDTO>> searchProjects(@RequestParam String name) {
        List<ProjectDTO> projects = projectService.getProjectsByName(name);
        return new ResponseEntity<>(projects, HttpStatus.OK);
    }

    @GetMapping("/startDate")
    public ResponseEntity<List<ProjectDTO>> searchProjectsByStartDate(@RequestParam LocalDateTime startDate) {
        List<ProjectDTO> projects = projectService.getProjectsByStartDate(startDate);
        return new ResponseEntity<>(projects, HttpStatus.OK);
    }

    private String buildErrorMessage(BindingResult bindingResult) {
        StringBuilder errMsg = new StringBuilder();
        List<FieldError> errors = bindingResult.getFieldErrors();
        for (FieldError error : errors) {
            errMsg.append(String.format("%s: %s;", error.getField(), error.getDefaultMessage()));
        }
        return errMsg.toString();
    }
}
