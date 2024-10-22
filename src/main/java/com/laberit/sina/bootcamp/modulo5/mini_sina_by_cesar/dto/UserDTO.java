package com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.dto;

import com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.dto.UserDTO;
import com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.model.User;
import com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.model.enumerations.UserRole;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {

    public UserDTO convertToDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getUsername(),
                user.getRole().name()
        );
    }

    public User convertToEntity(UserDTO userDTO) {
        return new User(
                userDTO.id(),
                userDTO.firstName(),
                userDTO.lastName(),
                userDTO.username(),
                UserRole.valueOf(userDTO.role())
        );
    }
}
