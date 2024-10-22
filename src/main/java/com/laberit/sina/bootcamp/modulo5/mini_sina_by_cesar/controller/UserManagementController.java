package com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.controller;

import com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.dto.UserDTO;
import com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.dto.converters.UserConverter;
import com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.exception.ResourceNotFoundException;
import com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.model.User;
import com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/mini-sina/v1/users")
@Tag(
        name = "UserManagementController",
        description = "Controller for managing users in the system."
)
public class UserManagementController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserConverter userConverter;

    /**
     * Retrieves all users, with pagination handled via query parameters.
     *
     * @param page Page number for pagination.
     * @param size Page size for pagination.
     * @param sortField Field to sort by.
     * @param sortDirection Sort direction (asc or desc).
     * @return A list of users.
     */
    @Operation(
            summary = "Get all users",
            description = "Retrieves all users in the system, with optional pagination parameters."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Users retrieved successfully",
                    content = { @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserDTO.class)
                    )}
            )
    })
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserDTO> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortField,
            @RequestParam(defaultValue = "asc") String sortDirection) {

        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));

        return userService.getAllUsers(pageable)
                .stream()
                .map(userConverter::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a user by ID.
     *
     * @param id ID of the user.
     * @return The user with the specified ID.
     */
    @Operation(
            summary = "Get user by ID",
            description = "Retrieves a user by their unique ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User retrieved successfully",
                    content = { @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserDTO.class)
                    )}
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found"
            )
    })
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDTO getUserById(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);
        return user.map(userConverter::convertToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    /**
     * Disallows user creation from this endpoint.
     *
     * @param userDTO User details in the request body.
     * @return A message indicating that user creation is not allowed.
     */
    @Operation(
            summary = "Disallow user creation",
            description = "This endpoint disallows user creation. Users should be created via the registration endpoint."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "405",
                    description = "Method not allowed"
            )
    })
    @PostMapping
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public String createUser(@RequestBody UserDTO userDTO) {
        return "User creation is not allowed in this endpoint. Please register users at mini-sina/v1/auth/register";
    }

    /**
     * Deletes a user by ID.
     *
     * @param id ID of the user to delete.
     * @return A response indicating whether the user was deleted.
     */
    @Operation(
            summary = "Delete user by ID",
            description = "Deletes a user by their unique ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "User deleted successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found"
            )
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long id) {
        if (!userService.deleteUser(id)) {
            throw new ResourceNotFoundException("User not found");
        }
    }
}
