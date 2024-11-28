package ru.roh.springdemo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.roh.springdemo.DTO.CommentDTO;
import ru.roh.springdemo.services.CommentService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping
    public ResponseEntity<List<CommentDTO>> getAllComments() {
        List<CommentDTO> comments = commentService.getAllComments();
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CommentDTO> createComment(@RequestParam Long taskId, @RequestParam Long userId, @RequestBody String text) {
        CommentDTO commentDTO = commentService.createComment(taskId, userId, text);
        return new ResponseEntity<>(commentDTO, HttpStatus.CREATED);
    }

    @GetMapping("/task/{taskId}")
    public ResponseEntity<List<CommentDTO>> getCommentsByTaskId(@PathVariable Long taskId) {
        List<CommentDTO> comments = commentService.getCommentsByTaskId(taskId);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CommentDTO>> getCommentsByUserId(@PathVariable Long userId) {
        List<CommentDTO> comments = commentService.getCommentsByUserId(userId);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    @GetMapping("/createdAfter")
    public ResponseEntity<List<CommentDTO>> getCommentsByCreatedAtAfter(@RequestParam LocalDateTime createdAt){
        List<CommentDTO> comments = commentService.getCommentsByCreatedAtAfter(createdAt);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }
}
