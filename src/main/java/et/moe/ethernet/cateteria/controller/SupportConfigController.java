package et.moe.ethernet.cateteria.controller;

import et.moe.ethernet.cateteria.entity.SupportConfig;
import et.moe.ethernet.cateteria.repository.SupportConfigRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Optional;

@RestController
@RequestMapping("/support-config")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class SupportConfigController {
    
    private final SupportConfigRepository supportConfigRepository;
    
    @GetMapping
    public ResponseEntity<SupportConfig> getSupportConfig() {
        Optional<SupportConfig> config = supportConfigRepository.findByIsActiveTrue();
        return config.map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<SupportConfig> createSupportConfig(@RequestBody SupportConfig supportConfig) {
        // Deactivate existing config
        supportConfigRepository.findByIsActiveTrue()
            .ifPresent(existingConfig -> {
                existingConfig.setActive(false);
                supportConfigRepository.save(existingConfig);
            });
        
        // Create new config
        supportConfig.setActive(true);
        SupportConfig savedConfig = supportConfigRepository.save(supportConfig);
        return ResponseEntity.ok(savedConfig);
    }
    
    @PutMapping("/max-salary")
    public ResponseEntity<SupportConfig> updateMaxSalaryForSupport(@RequestParam BigDecimal maxSalary) {
        // Deactivate existing config
        supportConfigRepository.findByIsActiveTrue()
            .ifPresent(existingConfig -> {
                existingConfig.setActive(false);
                supportConfigRepository.save(existingConfig);
            });
        
        // Create new config
        SupportConfig newConfig = new SupportConfig();
        newConfig.setMaxSalaryForSupport(maxSalary);
        newConfig.setActive(true);
        
        SupportConfig savedConfig = supportConfigRepository.save(newConfig);
        return ResponseEntity.ok(savedConfig);
    }
} 