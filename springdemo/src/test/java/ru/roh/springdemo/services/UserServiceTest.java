package ru.roh.springdemo.services;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import ru.roh.springdemo.DTO.UserDTO;
import ru.roh.springdemo.models.User;
import ru.roh.springdemo.repositories.UserRepository;
import ru.roh.springdemo.utils.NotCreatedException;
import ru.roh.springdemo.utils.NotFoundException;
import ru.roh.springdemo.utils.Role;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    private User user1;
    private User user2;
    private UserDTO userDTO1;
    private UserDTO userDTO2;

    @BeforeEach
    void setUp() {
        user1 = new User();
        user1.setFirstName("John");
        user1.setLastName("Doe");
        user1.setEmail("john.doe@example.com");
        user1.setRole(Role.USER);
        user2 = new User();
        user2.setFirstName("Mike");
        user2.setLastName("Lang");
        user2.setEmail("mike.lang@example.com");
        user2.setRole(Role.MANAGER);
        userDTO1 = new UserDTO();
        userDTO1.setFirstName("John");
        userDTO1.setLastName("Doe");
        userDTO1.setEmail("john.doe@example.com");
        userDTO1.setRole(Role.USER);
        userDTO2 = new UserDTO();
        userDTO2.setFirstName("Mike");
        userDTO2.setLastName("Lang");
        userDTO2.setEmail("mike.lang@example.com");
        userDTO2.setRole(Role.MANAGER);
    }

    @AfterEach
    void tearDown() {
    }

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("Метод возвращает список пользователей")
    void getAllUsers_returnsListOfUserDTOs() {
        List<User> userList = List.of(user1, user2);
        List<UserDTO> userDTOList = List.of(userDTO1, userDTO2);
        when(userRepository.findAll()).thenReturn(userList);
        when(modelMapper.map(userList.get(0), UserDTO.class)).thenReturn(userDTOList.get(0));
        when(modelMapper.map(userList.get(1), UserDTO.class)).thenReturn(userDTOList.get(1));

        List<UserDTO> result = userService.getAllUsers();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("John", result.get(0).getFirstName());
        assertEquals("Mike", result.get(1).getFirstName());
        verify(userRepository, times(1)).findAll();
        verify(modelMapper, times(2)).map(any(User.class), eq(UserDTO.class));
    }

    @Test
    @DisplayName("Метод возвращает пользователя по ID пользователей")
    void getUserById_returnsUserDTO_whenUserExists() {
        Long userID = 1L;
        when(userRepository.findById(userID)).thenReturn(Optional.of(user1));
        //when(modelMapper.map(any(UserDTO.class), eq(User.class))).thenReturn(user);
        when(modelMapper.map(any(User.class), eq(UserDTO.class))).thenReturn(userDTO1);

        UserDTO result = userService.getUserById(userID);

        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals(Role.USER, result.getRole());
        verify(modelMapper, times(1)).map(any(User.class), eq(UserDTO.class));
        verify(userRepository, times(1)).findById(userID);
    }

    @Test
    void getUserById_throwsNotFoundException_whenUserDoesNotExist() {
        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.getUserById(userId));
    }

    @Test
    void createUser_savesAndReturnsUserDTO_whenEmailIsUnique() {
        User savedUser = new User();
        savedUser.setFirstName("John");
        savedUser.setLastName("Doe");
        savedUser.setEmail("john.doe@example.com");
        savedUser.setRole(Role.USER);

        when(modelMapper.map(any(UserDTO.class), eq(User.class))).thenReturn(user1);
        when(modelMapper.map(any(User.class), eq(UserDTO.class))).thenReturn(userDTO1);
        when(userRepository.existsByEmail(userDTO1.getEmail())).thenReturn(false);
        when(userRepository.save(user1)).thenReturn(savedUser);

        UserDTO result = userService.createUser(userDTO1);

        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        verify(userRepository, times(1)).existsByEmail(userDTO1.getEmail());
        verify(userRepository, times(1)).save(user1);
        verify(modelMapper, times(1)).map(any(UserDTO.class), eq(User.class));
        verify(modelMapper, times(1)).map(any(User.class), eq(UserDTO.class));
    }

    @Test
    void createUser_throwsNotCreatedException_whenEmailAlreadyExists() {
        UserDTO userDTO = new UserDTO();

        when(userRepository.existsByEmail(userDTO.getEmail())).thenReturn(true);

        assertThrows(NotCreatedException.class, () -> userService.createUser(userDTO));
    }

    @Test
    void deleteUser_deletesUser_whenUserExists() {
        Long userId = 1L;

        when(userRepository.existsById(userId)).thenReturn(true);

        userService.deleteUser(userId);

        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    void deleteUser_throwsNotFoundException_whenUserDoesNotExist() {
        Long userId = 1L;

        when(userRepository.existsById(userId)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> userService.deleteUser(userId));
    }

    @Test
    void updateUser_updatesAndReturnsUserDTO_whenUserExists() {
        Long userId = 1L;

        User returnedUser = new User();
        returnedUser.setRole(Role.USER);
        returnedUser.setEmail("ret@mail.com");
        returnedUser.setFirstName("john");
        returnedUser.setLastName("lang");

        User updated = new User();
        updated.setRole(Role.USER);
        updated.setEmail("ret@mail.com");
        updated.setFirstName("john");
        updated.setLastName("lang");

        UserDTO userDTO = new UserDTO();
        userDTO.setRole(Role.USER);
        userDTO.setEmail("ret@mail.com");
        userDTO.setFirstName("john");
        userDTO.setLastName("lang");

        when(userRepository.findById(userId)).thenReturn(Optional.of(returnedUser));
        when(userRepository.save(any(User.class))).thenReturn(updated);
        when(modelMapper.map(any(User.class), eq(UserDTO.class))).thenReturn(userDTO);

        UserDTO result = userService.updateUser(userId, userDTO1);

        assertNotNull(result);
        assertEquals("lang", result.getLastName());
    }
}
