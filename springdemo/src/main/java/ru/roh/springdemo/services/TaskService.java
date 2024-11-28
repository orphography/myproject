package ru.roh.springdemo.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.roh.springdemo.DTO.TaskDTO;
import ru.roh.springdemo.models.Task;
import ru.roh.springdemo.repositories.ProjectRepository;
import ru.roh.springdemo.repositories.TaskRepository;
import ru.roh.springdemo.repositories.UserRepository;
import ru.roh.springdemo.utils.NotFoundException;
import ru.roh.springdemo.utils.Priority;
import ru.roh.springdemo.utils.Status;
import ru.roh.springdemo.utils.TaskChangeDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private TaskHistoryService taskHistoryService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private ModelMapper modelMapper;

    public List<TaskDTO> getAllTasks() {
        List<Task> tasks = taskRepository.findAll();
        return tasks.stream()
                .map(task -> modelMapper.map(task, TaskDTO.class))
                .collect(Collectors.toList());
    }

    public TaskDTO getTaskById(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new NotFoundException("Task with this ID not found"));
        TaskDTO returnTaskDTO = modelMapper.map(task, TaskDTO.class);
        return returnTaskDTO;
    }

    public TaskDTO createTask(TaskDTO taskDTO) {
        Task task = modelMapper.map(taskDTO, Task.class);
        task.setStartDate(LocalDateTime.now());
        task.setDueDate(LocalDateTime.now().plusDays(5));
        taskRepository.save(task);
        TaskDTO returnTaskDTO = modelMapper.map(task, TaskDTO.class);
        return returnTaskDTO;
    }
    @Transactional
    public TaskDTO updateTask(Long id, TaskDTO taskDTO) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new NotFoundException("Task with this ID not found"));

        List<TaskChangeDetails> changes = new ArrayList<>();

        if (!task.getTitle().equals(taskDTO.getTitle())) {
            changes.add(new TaskChangeDetails("Title", task.getTitle(), taskDTO.getTitle()));
            task.setTitle(taskDTO.getTitle());
        }
        if (!task.getDescription().equals(taskDTO.getDescription())) {
            changes.add(new TaskChangeDetails("Description", task.getDescription(), taskDTO.getDescription()));
            task.setDescription(taskDTO.getDescription());
        }
        if (!task.getPriority().equals(taskDTO.getPriority())) {
            changes.add(new TaskChangeDetails("Priority", task.getPriority(), taskDTO.getPriority()));
            task.setPriority(taskDTO.getPriority());
        }
        if (!task.getStatus().equals(taskDTO.getStatus())) {
            changes.add(new TaskChangeDetails("Status", task.getStatus(), taskDTO.getStatus()));
            task.setStatus(taskDTO.getStatus());
        }

        Task updatedTask = taskRepository.save(task);

        taskHistoryService.addTaskHistory(
                task.getId(),
                task.getAssigneeId(),
                changes
        );

        return modelMapper.map(updatedTask, TaskDTO.class);
    }

    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new NotFoundException("Task with this ID not found");
        }
        taskRepository.deleteById(id);
        taskHistoryService.deleteTaskHistory(id);
    }

    public List<TaskDTO> getTasksByStatus(Status status) {
        List<Task> tasks = taskRepository.findByStatus(status);
        return tasks.stream()
                .map(task -> modelMapper.map(task, TaskDTO.class))
                .collect(Collectors.toList());
    }

    public List<TaskDTO> getTasksByPriority(Priority priority) {
        List<Task> tasks = taskRepository.findByPriority(priority);
        return tasks.stream()
                .map(task -> modelMapper.map(task, TaskDTO.class))
                .collect(Collectors.toList());
    }

    public List<TaskDTO> getTasksByAssignee(Long userId) {
        List<Task> tasks = taskRepository.findByAssigneeId(userId);
        return tasks.stream()
                .map(task -> modelMapper.map(task, TaskDTO.class))
                .collect(Collectors.toList());
    }

    public List<TaskDTO> getTasksByProject(Long projectId) {
        List<Task> tasks = taskRepository.findByProjectId(projectId);
        return tasks.stream()
                .map(task -> modelMapper.map(task, TaskDTO.class))
                .collect(Collectors.toList());
    }

    public TaskDTO updateTaskStatus(Long taskId, Status newStatus) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NotFoundException("Task with ID " + taskId + " not found"));
        task.setStatus(newStatus);
        Task updatedTask = taskRepository.save(task);
        TaskDTO result = modelMapper.map(updatedTask, TaskDTO.class);
        return result;
    }

    public List<TaskDTO> getTasksByTitleContaining(String title) {
        List<Task> tasks = taskRepository.findByTitleContainingIgnoreCase(title);
        return tasks.stream()
                .map(task -> modelMapper.map(task, TaskDTO.class))
                .collect(Collectors.toList());
    }
}
