package ru.roh.springdemo.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.roh.springdemo.DTO.UserDTO;
import ru.roh.springdemo.services.UserService;
import ru.roh.springdemo.utils.NotFoundException;
import ru.roh.springdemo.utils.Role;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        userDTO = new UserDTO();
        userDTO.setFirstName("John");
        userDTO.setLastName("Doe");
        userDTO.setEmail("john.doe@example.com");
        userDTO.setRole(Role.USER);
    }

    @Test
    void getAllUsers_ShouldReturnListOfUserDTOs() throws Exception {
        when(userService.getAllUsers()).thenReturn(List.of(userDTO));

        mockMvc.perform(get("/api/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[0].email").value("john.doe@example.com"));

        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void getUserById_ShouldReturnUserDTO_WhenUserExists() throws Exception {
        Long userId = 1L;
        when(userService.getUserById(userId)).thenReturn(userDTO);

        mockMvc.perform(get("/api/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));

        verify(userService, times(1)).getUserById(userId);
    }

    @Test
    void createUser_ShouldReturnCreatedUserDTO_WhenInputIsValid() throws Exception {
        when(userService.createUser(any(UserDTO.class))).thenReturn(userDTO);

        String userJson = """
                    {
                        "firstName": "John",
                        "lastName": "Doe",
                        "email": "john.doe@example.com",
                        "role": "USER"
                    }
                """;

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));

        verify(userService, times(1)).createUser(any(UserDTO.class));
    }

    @Test
    void updateUser_ShouldReturnUpdatedUserDTO_WhenInputIsValid() throws Exception {
        Long userId = 1L;
        when(userService.updateUser(eq(userId), any(UserDTO.class))).thenReturn(userDTO);

        String userJson = """
                    {
                        "firstName": "John",
                        "lastName": "Doe",
                        "email": "john.doe@example.com",
                        "role": "USER"
                    }
                """;

        mockMvc.perform(put("/api/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));

        verify(userService, times(1)).updateUser(eq(userId), any(UserDTO.class));
    }

    @Test
    void deleteUser_ShouldReturnNoContent_WhenUserExists() throws Exception {
        Long userId = 1L;

        mockMvc.perform(delete("/api/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).deleteUser(userId);
    }

    @Test
    void getUserById_ShouldReturnNotFound_WhenUserDoesNotExist() throws Exception {
        Long userId = 1L;
        when(userService.getUserById(userId)).thenThrow(new NotFoundException("User not found"));

        mockMvc.perform(get("/api/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("User not found"));

        verify(userService, times(1)).getUserById(userId);
    }
}
