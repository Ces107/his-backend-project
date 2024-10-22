package com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.service.impl;

import com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.model.User;
import com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.model.enumerations.Permission;
import com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.model.enumerations.UserRole;
import com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.repository.UserRepository;
import com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.service.UserService;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public Page<User> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public Boolean deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        } else {
            return false;
        }
        return true;
    }



    public Set<String> assignPermissionsBasedOnRole(UserRole role) {
        Set<Permission> permissions = switch (role) {
            case ADMIN ->
                    EnumSet.of(
                            Permission.MANAGE_USERS,
                            Permission.VIEW_PATIENTS,
                            Permission.MANAGE_DIAGNOSIS,
                            Permission.MANAGE_APPOINTMENTS,
                            Permission.VIEW_ANALYTICS);

            case DOCTOR ->
                    EnumSet.of(
                            Permission.VIEW_PATIENTS,
                            Permission.MANAGE_DIAGNOSIS,
                            Permission.MANAGE_APPOINTMENTS,
                            Permission.CREATE_PATIENT_APPOINTMENT,
                            Permission.UPDATE_APPOINTMENT_STATUS,
                            Permission.VIEW_DIAGNOSIS);

            case MANAGER -> EnumSet.of(
                    Permission.VIEW_PATIENTS,
                    Permission.VIEW_ANALYTICS,
                    Permission.VIEW_DIAGNOSIS);

            case PATIENT ->
                    EnumSet.of(
                            Permission.CREATE_APPOINTMENTS,
                            Permission.CANCEL_OWN_APPOINTMENT,
                            Permission.VIEW_OWN_APPOINTMENTS);

        };
        return permissions.stream().map(Permission::name).collect(Collectors.toSet());
    }

}