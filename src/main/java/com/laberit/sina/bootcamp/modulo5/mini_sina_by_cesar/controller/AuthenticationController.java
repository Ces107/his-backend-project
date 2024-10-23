package com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.controller;

import com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.dto.AuthDTO;
import com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.dto.LoginDTO;
import com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.dto.RegisterDTO;
import com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.model.User;
import com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.repository.UserRepository;
import com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.configuration.security.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/mini-sina/v1/auth")
@Tag(
        name = "Authentication",
        description = "Controller for handling user authentication, including login and registration."
)
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenService tokenService;

    /**
     * Endpoint for user login.
     *
     * @param data AuthDTO containing the login credentials.
     * @return JWT token if login is successful.
     */
    @Operation(
            summary = "User Login",
            description = "Authenticates a user with login and password, and returns a JWT token."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Login successful",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LoginDTO.class)
                    )}
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid login or password"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error"
            )
    })
    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<LoginDTO> login(@RequestBody @Valid AuthDTO data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        String token = tokenService.generateToken((User) auth.getPrincipal());
        return ResponseEntity.ok(new LoginDTO(token));
    }

    /**
     * Endpoint for user registration.
     *
     * @param data RegisterDTO containing user registration details.
     * @return Success message if registration is successful.
     */
    @Operation(
            summary = "User Registration",
            description = "Registers a new user in the system with a unique username."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User registered successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Username already exists"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error"
            )
    })
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> register(@RequestBody @Valid RegisterDTO data) {
        if (this.userRepository.existsByUsername(data.login())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username already exists");
        }

        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
        User newUser = new User(data.firstName(), data.lastName(), data.login(), encryptedPassword, data.role(), data.permissions());
        this.userRepository.save(newUser);

        return ResponseEntity.ok("User registered successfully");
    }
}
