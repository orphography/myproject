package ru.roh.springdemo.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.roh.springdemo.DTO.CommentDTO;
import ru.roh.springdemo.models.Comment;
import ru.roh.springdemo.models.Task;
import ru.roh.springdemo.models.User;
import ru.roh.springdemo.repositories.CommentRepository;
import ru.roh.springdemo.repositories.TaskRepository;
import ru.roh.springdemo.repositories.UserRepository;
import ru.roh.springdemo.utils.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;

    public List<CommentDTO> getAllComments() {
        List<Comment> comments = commentRepository.findAll();
        return comments.stream()
                .map(comment -> modelMapper.map(comment, CommentDTO.class))
                .collect(Collectors.toList());
    }

    public CommentDTO createComment(Long taskId, Long userId, String text) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NotFoundException("Task not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        Comment comment = new Comment();
        comment.setCreatedAt(LocalDateTime.now());
        comment.setUserId(user.getId());
        comment.setTaskId(task.getId());
        comment.setText(text);
        commentRepository.save(comment);
        return modelMapper.map(comment, CommentDTO.class);
    }

    public List<CommentDTO> getCommentsByTaskId(Long taskId) {
        List<Comment> comments = commentRepository.findByTaskId(taskId);
        return comments.stream()
                .map(comment -> modelMapper.map(comment, CommentDTO.class))
                .collect(Collectors.toList());
    }

    public List<CommentDTO> getCommentsByUserId(Long userId) {
        List<Comment> comments = commentRepository.findByUserId(userId);
        return comments.stream()
                .map(comment -> modelMapper.map(comment, CommentDTO.class))
                .collect(Collectors.toList());
    }

    public List<CommentDTO> getCommentsByCreatedAtAfter(LocalDateTime createdAt) {
        List<Comment> comments = commentRepository.findByCreatedAtAfter(createdAt);
        return comments.stream()
                .map(comment -> modelMapper.map(comment, CommentDTO.class))
                .collect(Collectors.toList());
    }

    public void deleteComment(Long id) {
        if (!commentRepository.existsById(id)) {
            throw new NotFoundException("Comment with this ID not found");
        }
        commentRepository.deleteById(id);
    }
}

