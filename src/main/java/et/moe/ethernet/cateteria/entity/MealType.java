package et.moe.ethernet.cateteria.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "meal_types")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MealType {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(length = 50)
    private String id;
    
    @Column(nullable = false, length = 100)
    private String name;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MealIcon icon = MealIcon.UTENSILS;
    
    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;
    
    @Column(nullable = false, length = 100)
    private String color = "bg-emerald-100 text-emerald-700";
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @OneToMany(mappedBy = "mealType", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MealCategory> mealCategories;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    public enum MealIcon {
        COFFEE, UTENSILS, MOON;
        
        @JsonValue
        public String toLowerCase() {
            return this.name().toLowerCase();
        }
        
        @JsonCreator
        public static MealIcon fromString(String value) {
            if (value == null) return null;
            // Convert to uppercase to match enum values
            String upperValue = value.toUpperCase();
            try {
                return MealIcon.valueOf(upperValue);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Unknown MealIcon: " + value + ". Valid values are: " + 
                    java.util.Arrays.toString(MealIcon.values()).toLowerCase());
            }
        }
    }
} 