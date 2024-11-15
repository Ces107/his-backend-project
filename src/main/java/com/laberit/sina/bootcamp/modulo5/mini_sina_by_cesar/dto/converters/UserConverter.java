package com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.dto.converters;

import com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.dto.UserDTO;
import com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.model.User;
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

}

