package com.smartlibrary.service;

import com.smartlibrary.dto.UserDto;
import com.smartlibrary.entity.User;
import com.smartlibrary.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return convertToDto(user);
    }
    
    public UserDto createUser(UserDto userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPhone(userDto.getPhone());
        user.setPassword("defaultPassword"); // Set a default password
        user.setRole(User.UserRole.valueOf(userDto.getRole()));
        user.setActive(userDto.getActive() != null ? userDto.getActive() : true);
        
        User savedUser = userRepository.save(user);
        return convertToDto(savedUser);
    }
    
    public Optional<UserDto> updateUser(Long id, UserDto userDto) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setUsername(userDto.getUsername());
                    user.setName(userDto.getName());
                    user.setEmail(userDto.getEmail());
                    user.setPhone(userDto.getPhone());
                    // Don't update password here for security
                    user.setRole(User.UserRole.valueOf(userDto.getRole()));
                    user.setActive(userDto.getActive() != null ? userDto.getActive() : user.getActive());
                    return convertToDto(userRepository.save(user));
                });
    }
    
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
    
    public UserDto deactivateUser(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found"));
        user.setActive(false);
        userRepository.save(user);
        return convertToDto(user);
    }

    public UserDto activateUser(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found"));
        user.setActive(true);
        userRepository.save(user);
        return convertToDto(user);
    }
    
    private UserDto convertToDto(User user) {
        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getName(),
                user.getEmail(),
                user.getPhone(),
                user.getRole().name(),
                user.getActive(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
} 