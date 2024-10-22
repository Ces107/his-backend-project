package com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.service;

import com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service to manage users.
 * @see com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.service.UserService
 * @see com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.model.User
 */
public interface UserService {
    Page<User> getAllUsers(Pageable pageable);
    Optional<User> getUserById(Long id);
    User saveUser(User user);
    Boolean deleteUser(Long id);
}