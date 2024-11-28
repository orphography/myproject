package ru.roh.springdemo.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.roh.springdemo.DTO.TaskHistoryDTO;
import ru.roh.springdemo.models.Task;
import ru.roh.springdemo.models.TaskHistory;
import ru.roh.springdemo.models.User;
import ru.roh.springdemo.repositories.TaskHistoryRepository;
import ru.roh.springdemo.repositories.TaskRepository;
import ru.roh.springdemo.repositories.UserRepository;
import ru.roh.springdemo.utils.NotFoundException;
import ru.roh.springdemo.utils.TaskChangeDetails;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskHistoryService {

    @Autowired
    private TaskHistoryRepository taskHistoryRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;

    public List<TaskHistoryDTO> getAll() {
        List<TaskHistory> taskHistories = taskHistoryRepository.findAll();
        return taskHistories.stream()
                .map(taskHistory -> modelMapper.map(taskHistory, TaskHistoryDTO.class))
                .collect(Collectors.toList());
    }
    public List<TaskHistoryDTO> getHistoryByTaskId(Long taskId) {
        List<TaskHistory> taskHistories = taskHistoryRepository.findByTaskId(taskId);
        return taskHistories.stream()
                .map(taskHistory -> modelMapper.map(taskHistory, TaskHistoryDTO.class))
                .collect(Collectors.toList());
    }

    public void addTaskHistory(Long taskId, Long changedBy, List<TaskChangeDetails> changeDetails) {
        TaskHistory history = new TaskHistory();
        history.setTaskId(taskId);
        history.setChangedBy(changedBy);
        history.setChangeDate(LocalDateTime.now());
        history.setChangeDetails(changeDetails);
        taskHistoryRepository.save(history);
    }

    public void deleteTaskHistory(Long taskId) {

        taskHistoryRepository.deleteByTaskId(taskId);
    }
}
