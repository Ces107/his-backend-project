package com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.dto;

import com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.model.enumerations.UserRole;
import java.util.Set;

public record RegisterDTO(String firstName, String lastName, String login, String password, UserRole role, Set<String> permissions) { }
