package ru.roh.springdemo.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.roh.springdemo.utils.Role;

@Data
@Schema(description = "Модель данных пользователя")
public class UserDTO {
    @NotBlank(message = "Firstname is mandatory")
    @Size(min = 2, max = 30, message = "First name must be between 2 and 30 characters")
    @Schema(description = "Имя пользователя", example = "John")
    private String firstName;
    @NotBlank(message = "Last name is mandatory")
    @Size(min = 2, max = 30, message = "Last name must be between 2 and 30 characters")
    @Schema(description = "Фамилия пользователя", example = "Doe")
    private String lastName;
    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    @Schema(description = "Email пользователя", example = "john.doe@example.com")
    private String email;
    @NotNull(message = "Role can't be null")
    @Schema(description = "Роль пользователя", example = "USER")
    private Role role;
}
