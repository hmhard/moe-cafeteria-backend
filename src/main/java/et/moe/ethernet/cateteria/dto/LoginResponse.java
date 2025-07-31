package et.moe.ethernet.cateteria.dto;

import et.moe.ethernet.cateteria.entity.User;
import lombok.Data;

@Data
public class LoginResponse {
    
    private User user;
    private String message;
    private boolean success;
} 