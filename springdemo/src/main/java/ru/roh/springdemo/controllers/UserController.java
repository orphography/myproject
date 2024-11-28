package ru.roh.springdemo.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.roh.springdemo.DTO.UserDTO;
import ru.roh.springdemo.services.UserService;
import ru.roh.springdemo.utils.NotCreatedException;
import ru.roh.springdemo.utils.NotUpdateException;
import ru.roh.springdemo.utils.Role;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Пользователи", description = "Управление пользователями")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    @Operation(summary = "Получить всех пользователей", description = "Возвращает список всех пользователей")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить пользователя по ID", description = "Возвращает данные пользователя по его ID")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        UserDTO userDTO = userService.getUserById(id);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @PostMapping
    @Operation(summary = "Создать нового пользователя", description = "Добавляет нового пользователя в систему")
    public ResponseEntity<UserDTO> createUser(@RequestBody @Valid UserDTO userDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new NotCreatedException(buildErrorMessage(bindingResult));
        }
        UserDTO actualUser = userService.createUser(userDTO);
        return new ResponseEntity<>(actualUser, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить пользователя", description = "Обновляет данные пользователя")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @Valid @RequestBody UserDTO userDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new NotUpdateException(buildErrorMessage(bindingResult));
        }
        UserDTO updatedUser = userService.updateUser(id, userDTO);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить пользователя", description = "Удаляет пользователя из системы")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/role/{role}")
    @Operation(summary = "Поиск пользователей по роли", description = "Возвращает всех пользователей с указанной ролью")
    public ResponseEntity<List<UserDTO>> getUserByRole(@PathVariable Role role) {
        List<UserDTO> users = userService.getUsersByRole(role);
        return new ResponseEntity<>(users, HttpStatus.OK);
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
