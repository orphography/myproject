package ru.roh.springdemo.services;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import ru.roh.springdemo.DTO.TaskDTO;
import ru.roh.springdemo.models.Task;
import ru.roh.springdemo.models.TaskHistory;
import ru.roh.springdemo.repositories.TaskRepository;
import ru.roh.springdemo.utils.NotFoundException;
import ru.roh.springdemo.utils.Priority;
import ru.roh.springdemo.utils.Status;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    Task task;
    TaskDTO taskDTO;

    @BeforeEach
    void setUp() {
        task = new Task();
        task.setTitle("title");
        task.setDescription("description");
        task.setStatus(Status.NEW);
        task.setPriority(Priority.LOW);
        taskDTO = new TaskDTO();
        taskDTO.setTitle("title");
        taskDTO.setDescription("description");
        taskDTO.setStatus(Status.NEW);
        taskDTO.setPriority(Priority.LOW);
    }

    @AfterEach
    void tearDown() {
    }

    @Mock
    private ModelMapper modelMapper;
    @Mock
    private TaskHistoryService taskHistoryService;
    @Mock
    private TaskRepository taskRepository;
    @InjectMocks
    private TaskService taskService;

    @Test
    void getTaskById_returnsTaskDTO_whenTaskExists() {
        Long taskId = 1L;
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(modelMapper.map(any(Task.class), eq(TaskDTO.class))).thenReturn(taskDTO);

        TaskDTO result = taskService.getTaskById(taskId);

        assertNotNull(result);
        assertEquals(Status.NEW, result.getStatus());
        assertEquals("title", result.getTitle());
        verify(taskRepository, times(1)).findById(taskId);
        verify(modelMapper, times(1)).map(task, TaskDTO.class);
    }

    @Test
    void getTaskById_ShouldThrowNotFoundException_WhenTaskDoesNotExist() {
        Long taskId = 1L;

        Mockito.when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> taskService.getTaskById(taskId));
        assertEquals("Task with this ID not found", exception.getMessage());
        verify(taskRepository, times(1)).findById(taskId);
    }

    @Test
    void getAllTasks_returnsListOfTaskDTOs() {
        List<Task> taskList = List.of(task);
        List<TaskDTO> taskDTOList = List.of(taskDTO);
        when(taskRepository.findAll()).thenReturn(taskList);
        when(modelMapper.map(taskList.get(0), TaskDTO.class)).thenReturn(taskDTOList.get(0));

        List<TaskDTO> result = taskService.getAllTasks();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(Status.NEW, result.get(0).getStatus());
        assertEquals("title", result.get(0).getTitle());
        verify(taskRepository, times(1)).findAll();
        verify(modelMapper, times(1)).map(any(Task.class), eq(TaskDTO.class));
    }

    @Test
    void createTask_ShouldSaveTaskAndReturnTaskDTO() {
        TaskDTO inputTaskDTO = new TaskDTO();
        inputTaskDTO.setTitle("title");
        inputTaskDTO.setDescription("desc");
        inputTaskDTO.setStatus(Status.NEW);
        inputTaskDTO.setPriority(Priority.MEDIUM);
        Task taskToSave = new Task();
        taskToSave.setTitle("title");
        taskToSave.setDescription("desc");
        taskToSave.setStatus(Status.NEW);
        taskToSave.setPriority(Priority.MEDIUM);
        Task savedTask = new Task();
        savedTask.setTitle("title");
        savedTask.setDescription("desc");
        savedTask.setStatus(Status.NEW);
        savedTask.setPriority(Priority.MEDIUM);
        TaskDTO expectedTaskDTO = new TaskDTO();
        expectedTaskDTO.setTitle("title");
        expectedTaskDTO.setDescription("desc");
        expectedTaskDTO.setStatus(Status.NEW);
        expectedTaskDTO.setPriority(Priority.MEDIUM);


        when(modelMapper.map(any(TaskDTO.class), eq(Task.class))).thenReturn(taskToSave);
        when(taskRepository.save(any(Task.class))).thenReturn(savedTask);
        when(modelMapper.map(any(Task.class), eq(TaskDTO.class))).thenReturn(expectedTaskDTO);

        TaskDTO result = taskService.createTask(inputTaskDTO);

        assertNotNull(result);
        assertEquals(expectedTaskDTO.getTitle(), result.getTitle());
        assertEquals(expectedTaskDTO.getDescription(), result.getDescription());
        assertEquals(expectedTaskDTO.getStatus(), result.getStatus());
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void updateTask_shouldUpdateAndReturnUpdatedTaskDTO_whenTaskExists() {
        Long taskId = 1L;
        TaskDTO inputTaskDTO = new TaskDTO();
        inputTaskDTO.setTitle("updated");
        inputTaskDTO.setDescription("updated");
        inputTaskDTO.setStatus(Status.IN_PROGRESS);
        inputTaskDTO.setPriority(Priority.HIGH);
        Task existingTask = new Task();
        existingTask.setTitle("old");
        existingTask.setDescription("old");
        existingTask.setStatus(Status.NEW);
        existingTask.setPriority(Priority.LOW);
        Task updatedTask = new Task();
        updatedTask.setTitle("updated");
        updatedTask.setDescription("updated");
        updatedTask.setStatus(Status.IN_PROGRESS);
        updatedTask.setPriority(Priority.HIGH);
        TaskDTO expectedTaskDTO = new TaskDTO();
        expectedTaskDTO.setTitle("updated");
        expectedTaskDTO.setDescription("updated");
        expectedTaskDTO.setStatus(Status.IN_PROGRESS);
        expectedTaskDTO.setPriority(Priority.HIGH);
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(existingTask)).thenReturn(updatedTask);
        when(modelMapper.map(updatedTask, TaskDTO.class)).thenReturn(expectedTaskDTO);

        TaskDTO result = taskService.updateTask(taskId, inputTaskDTO);

        assertNotNull(result);
        assertEquals("updated", result.getTitle());
        assertEquals("updated", result.getDescription());
        assertEquals(Priority.HIGH, result.getPriority());
        assertEquals(Status.IN_PROGRESS, result.getStatus());
        verify(taskRepository, times(1)).findById(taskId);
        verify(taskRepository, times(1)).save(existingTask);
        verify(modelMapper, times(1)).map(updatedTask, TaskDTO.class);
    }

    @Test
    void updateTask_ShouldThrowNotFoundException_WhenTaskDoesNotExist() {
        Long taskId = 1L;
        TaskDTO inputTaskDTO = new TaskDTO();
        inputTaskDTO.setTitle("updated");
        inputTaskDTO.setDescription("updated");
        inputTaskDTO.setStatus(Status.IN_PROGRESS);
        inputTaskDTO.setPriority(Priority.HIGH);

        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> taskService.updateTask(taskId, inputTaskDTO));
        assertEquals("Task with this ID not found", exception.getMessage());
        verify(taskRepository, times(1)).findById(taskId);
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    void deleteTask_ShouldDeleteTask_WhenTaskExists() {
        Long taskId = 1L;
        when(taskRepository.existsById(taskId)).thenReturn(true);

        taskService.deleteTask(taskId);

        verify(taskRepository, times(1)).existsById(taskId);
        verify(taskRepository, times(1)).deleteById(taskId);
    }

    //
    @Test
    void deleteTask_ShouldThrowNotFoundException_WhenTaskDoesNotExist() {
        Long taskId = 1L;

        when(taskRepository.existsById(taskId)).thenReturn(false);

        NotFoundException exception = assertThrows(NotFoundException.class, () -> taskService.deleteTask(taskId));
        assertEquals("Task with this ID not found", exception.getMessage());
        verify(taskRepository, times(1)).existsById(taskId);
        verify(taskRepository, never()).deleteById(taskId);
    }
    @Test
    void updateTaskStatus_UpdatesStatusSuccessfully() {
        Task task1 =  new Task();
        task1.setStatus(Status.IN_PROGRESS);
        TaskDTO taskDTO1 = new TaskDTO();
        taskDTO1.setStatus(Status.IN_PROGRESS);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task1);
        when(modelMapper.map(any(Task.class), eq(TaskDTO.class))).thenReturn(taskDTO1);

        TaskDTO updatedTask = taskService.updateTaskStatus(1L, Status.IN_PROGRESS);

        assertEquals(Status.IN_PROGRESS, updatedTask.getStatus());
        verify(taskRepository).save(task);
    }
}