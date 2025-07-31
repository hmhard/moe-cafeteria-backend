package et.moe.ethernet.cateteria.service;

import et.moe.ethernet.cateteria.entity.User;
import et.moe.ethernet.cateteria.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsernameAndIsActiveTrue(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }
    
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    public Optional<User> getUserById(String id) {
        return userRepository.findById(id);
    }
    
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    public User createUser(User user) {
        // Check if username or email already exists
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        
        // Encode password
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        return userRepository.save(user);
    }
    
    public Optional<User> updateUser(String id, User userUpdates) {
        return userRepository.findById(id)
            .map(existingUser -> {
                // Check for unique constraint violations
                if (!existingUser.getUsername().equals(userUpdates.getUsername()) &&
                    userRepository.existsByUsername(userUpdates.getUsername())) {
                    throw new RuntimeException("Username already exists");
                }
                if (!existingUser.getEmail().equals(userUpdates.getEmail()) &&
                    userRepository.existsByEmail(userUpdates.getEmail())) {
                    throw new RuntimeException("Email already exists");
                }
                
                existingUser.setUsername(userUpdates.getUsername());
                existingUser.setEmail(userUpdates.getEmail());
                existingUser.setFullName(userUpdates.getFullName());
                existingUser.setRole(userUpdates.getRole());
                existingUser.setActive(userUpdates.isActive());
                
                // Only update password if provided
                if (userUpdates.getPassword() != null && !userUpdates.getPassword().isEmpty()) {
                    existingUser.setPassword(passwordEncoder.encode(userUpdates.getPassword()));
                }
                
                return userRepository.save(existingUser);
            });
    }
    
    public void updateLastLogin(String username) {
        userRepository.findByUsername(username)
            .ifPresent(user -> {
                user.setLastLogin(LocalDateTime.now());
                userRepository.save(user);
            });
    }
    
    public Optional<User> toggleUserStatus(String id) {
        return userRepository.findById(id)
            .map(user -> {
                user.setActive(!user.isActive());
                return userRepository.save(user);
            });
    }
    
    public boolean deleteUser(String id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }
} 