package com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.controller;

import com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.dto.AuthDTO;
import com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.dto.LoginDTO;
import com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.dto.RegisterDTO;
import com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.model.User;
import com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.repository.UserRepository;
import com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.configuration.security.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<LoginDTO> login(@RequestBody @Valid AuthDTO data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        String token = tokenService.generateToken((User) auth.getPrincipal());
        return ResponseEntity.ok(new LoginDTO(token));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterDTO data) {
        if (this.userRepository.existsByUsername(data.login())) {
            return ResponseEntity.badRequest().body("Username already exists");
        }

        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());

        User newUser = new User(data.firstName(), data.lastName(), data.login(), encryptedPassword, data.role(), data.permissions());
        this.userRepository.save(newUser);

        return ResponseEntity.ok("User registered successfully");
    }
}
