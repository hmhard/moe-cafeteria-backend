package et.moe.ethernet.cateteria.controller;

import et.moe.ethernet.cateteria.dto.UserDto;
import et.moe.ethernet.cateteria.entity.User;
import et.moe.ethernet.cateteria.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "User management endpoints")
public class UserController {

    private final UserService userService;

    @GetMapping
    @Operation(summary = "Get all users", description = "Retrieve all users from the system")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = userService.getAllUsers().stream()
            .map(UserDto::fromEntity)
            .collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID", description = "Retrieve a specific user by their ID")
    public ResponseEntity<UserDto> getUserById(@PathVariable String id) {
        return userService.getUserById(id)
                .map(UserDto::fromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create user", description = "Create a new user")
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody User user) {
        User createdUser = userService.createUser(user);
        return ResponseEntity.ok(UserDto.fromEntity(createdUser));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update user", description = "Update an existing user")
    public ResponseEntity<UserDto> updateUser(@PathVariable String id, @Valid @RequestBody User user) {
        return userService.updateUser(id, user)
                .map(UserDto::fromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user", description = "Delete a user by ID")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        if (userService.deleteUser(id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}/toggle")
    @Operation(summary = "Toggle user status", description = "Toggle the active status of a user")
    public ResponseEntity<UserDto> toggleUserStatus(@PathVariable String id) {
        return userService.toggleUserStatus(id)
                .map(UserDto::fromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
} 