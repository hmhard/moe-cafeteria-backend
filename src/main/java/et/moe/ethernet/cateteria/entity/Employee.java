package et.moe.ethernet.cateteria.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "employees")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    @Column(name = "employee_id", unique = true, nullable = false, length = 50)
    private String employeeId;
    
    @Column(name = "card_id", unique = true)
    private String cardId;
    
    @Column(name = "short_code", unique = true, length = 4)
    private String shortCode;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false, length = 100)
    private String department;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal salary;
    
    @Column(name = "photo_url")
    private String photoUrl;
    
    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MealRecord> mealRecords;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
} 