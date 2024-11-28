package ru.roh.springdemo.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.roh.springdemo.utils.Role;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "Users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotEmpty(message = "Firstname should not be empty")
    @Size(min = 2, max = 30, message = "Firstname should be between 2 and 30 characters")
    private String firstName;
    @NotEmpty(message = "Lastname should not be empty")
    @Size(min = 2, max = 30, message = "Lastname should be between 2 and 30 characters")
    private String lastName;
    @NotEmpty(message = "Email should not be empty")
    @Email(message = "Email must have the format of an email address")
    private String email;
    @Enumerated(EnumType.STRING)
    private Role role;
    @JsonIgnore
    private String password;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
