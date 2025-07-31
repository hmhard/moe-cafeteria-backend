package et.moe.ethernet.cateteria.controller;

import et.moe.ethernet.cateteria.dto.LoginRequest;
import et.moe.ethernet.cateteria.dto.LoginResponse;
import et.moe.ethernet.cateteria.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication endpoints")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    @PostMapping("/login")
    @Operation(summary = "User login", description = "Authenticate user with username and password")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            // Authenticate user
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(),
                    loginRequest.getPassword()
                )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Get user details
            var userDetails = userService.loadUserByUsername(loginRequest.getUsername());
            var userOptional = userService.findByUsername(loginRequest.getUsername());
            
            if (userOptional.isEmpty()) {
                LoginResponse response = new LoginResponse();
                response.setMessage("User not found");
                response.setSuccess(false);
                return ResponseEntity.badRequest().body(response);
            }

            LoginResponse response = new LoginResponse();
            response.setUser(userOptional.get());
            response.setMessage("Login successful");
            response.setSuccess(true);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            LoginResponse response = new LoginResponse();
            response.setMessage("Invalid username or password");
            response.setSuccess(false);
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/logout")
    @Operation(summary = "User logout", description = "Logout current user")
    public ResponseEntity<LoginResponse> logout() {
        SecurityContextHolder.clearContext();
        
        LoginResponse response = new LoginResponse();
        response.setMessage("Logout successful");
        response.setSuccess(true);
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    @Operation(summary = "Get current user", description = "Get details of currently authenticated user")
    public ResponseEntity<LoginResponse> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication != null && authentication.isAuthenticated()) {
            var userOptional = userService.findByUsername(authentication.getName());
            
            if (userOptional.isPresent()) {
                LoginResponse response = new LoginResponse();
                response.setUser(userOptional.get());
                response.setMessage("User authenticated");
                response.setSuccess(true);
                
                return ResponseEntity.ok(response);
            }
        }
        
        LoginResponse response = new LoginResponse();
        response.setMessage("Not authenticated");
        response.setSuccess(false);
        
        return ResponseEntity.status(401).body(response);
    }
} 