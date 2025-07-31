package et.moe.ethernet.cateteria.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import et.moe.ethernet.cateteria.entity.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    
    private String id;
    private String username;
    private String email;
    private String fullName;
    private String role;
    
    @JsonProperty("isActive")
    private boolean isActive;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime lastLogin;
    
    public static UserDto fromEntity(User user) {
        return new UserDto(
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            user.getFullName(),
            user.getRole().name(),
            user.isActive(),
            user.getCreatedAt(),
            user.getUpdatedAt(),
            user.getLastLogin()
        );
    }
} 