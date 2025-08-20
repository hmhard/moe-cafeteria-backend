package et.moe.ethernet.cateteria.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "meal_categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MealCategory {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meal_type_id", nullable = false)
    private MealType mealType;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MealCategoryType category;
    
    @Column(nullable = false, length = 100)
    private String name;
    
    @Column(name = "normal_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal normalPrice;
    
    @Column(name = "supported_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal supportedPrice;
    
    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @OneToMany(mappedBy = "mealCategory", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MealItem> mealItems;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    public enum MealCategoryType {
        FASTING, NON_FASTING;
        
        @JsonValue
        public String toLowerCase() {
            return this.name().toLowerCase();
        }
        
        @JsonCreator
        public static MealCategoryType fromString(String value) {
            if (value == null) return null;
            // Convert to uppercase to match enum values
            String upperValue = value.toUpperCase();
            try {
                return MealCategoryType.valueOf(upperValue);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Unknown MealCategoryType: " + value + ". Valid values are: " + 
                    java.util.Arrays.toString(MealCategoryType.values()).toLowerCase());
            }
        }
    }
} 