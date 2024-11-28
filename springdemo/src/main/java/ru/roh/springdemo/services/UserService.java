package ru.roh.springdemo.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.roh.springdemo.DTO.UserDTO;
import ru.roh.springdemo.models.User;
import ru.roh.springdemo.repositories.UserRepository;
import ru.roh.springdemo.utils.NotCreatedException;
import ru.roh.springdemo.utils.NotFoundException;
import ru.roh.springdemo.utils.Role;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;

    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .collect(Collectors.toList());
    }

    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User with this ID not found"));
        UserDTO returnUserDTO = modelMapper.map(user, UserDTO.class);
        return returnUserDTO;
    }

    public UserDTO createUser(UserDTO userDTO) {
        if (!userRepository.existsByEmail(userDTO.getEmail())) {
            User user = modelMapper.map(userDTO, User.class);
            user.setPassword("default_password");
            user.setCreatedAt(LocalDateTime.now());
            user.setUpdatedAt(LocalDateTime.now());
            userRepository.save(user);
            UserDTO returnUserDTO = modelMapper.map(user, UserDTO.class);
            return returnUserDTO;
        } else throw new NotCreatedException("A user with this email already exists.");
    }

    @Transactional
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User with this Id not found"));
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setRole(userDTO.getRole());
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
        UserDTO returnUserDTO = modelMapper.map(user, UserDTO.class);
        return returnUserDTO;
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("User with this ID not found");
        }
        userRepository.deleteById(id);
    }

    public List<UserDTO> getUsersByRole(Role role) {
        List<User> users = userRepository.findByRole(role);
        return users.stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .collect(Collectors.toList());
    }
}

