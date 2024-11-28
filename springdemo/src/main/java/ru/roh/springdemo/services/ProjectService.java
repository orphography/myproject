package ru.roh.springdemo.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.roh.springdemo.DTO.ProjectDTO;
import ru.roh.springdemo.models.Project;
import ru.roh.springdemo.repositories.ProjectRepository;
import ru.roh.springdemo.utils.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private ModelMapper modelMapper;

    public List<ProjectDTO> getAllProjects() {
        List<Project> projects = projectRepository.findAll();
        return projects.stream()
                .map(project -> modelMapper.map(project, ProjectDTO.class))
                .collect(Collectors.toList());
    }

    public ProjectDTO getProjectById(Long id) {
        Project project = projectRepository.findById(id).orElseThrow(() -> new NotFoundException("Project with this ID not found"));
        return modelMapper.map(project, ProjectDTO.class);
    }

    public ProjectDTO createProject(ProjectDTO projectDTO) {
        Project project = modelMapper.map(projectDTO, Project.class);
        project.setStartDate(LocalDateTime.now());
        project.setEndDate(LocalDateTime.now().plusDays(5));
        projectRepository.save(project);
        return modelMapper.map(project, ProjectDTO.class);
    }

    @Transactional
    public ProjectDTO updateProject(Long id, ProjectDTO projectDTO) {
        Project project = projectRepository.findById(id).orElseThrow(() -> new NotFoundException("Project with this ID not found"));
        project.setName(projectDTO.getName());
        project.setDescription(projectDTO.getDescription());
        projectRepository.save(project);
        return modelMapper.map(project, ProjectDTO.class);
    }

    public void deleteProject(Long id) {
        if (!projectRepository.existsById(id)) {
            throw new NotFoundException("Project with this ID not found");
        }
        projectRepository.deleteById(id);
    }

    public List<ProjectDTO> getProjectsByName(String name) {
        List<Project> projects = projectRepository.findByNameContainingIgnoreCase(name);
        return projects.stream()
                .map(project -> modelMapper.map(project, ProjectDTO.class))
                .collect(Collectors.toList());
    }

    public List<ProjectDTO> getProjectsByStartDate(LocalDateTime startDate) {
        List<Project> projects = projectRepository.findByStartDateAfter(startDate);
        return projects.stream()
                .map(project -> modelMapper.map(project, ProjectDTO.class))
                .collect(Collectors.toList());
    }
}
