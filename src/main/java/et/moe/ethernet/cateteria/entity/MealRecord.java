package et.moe.ethernet.cateteria.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "meal_records")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MealRecord {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;
    
    @Column(name = "card_id", nullable = false)
    private String cardId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meal_type_id", nullable = false)
    private MealType mealType;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meal_category_id", nullable = false)
    private MealCategory mealCategory;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recorded_by_user_id")
    private User recordedByUser;
    
    @Column(name = "meal_name", nullable = false, length = 100)
    private String mealName;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MealCategory.MealCategoryType category;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "price_type", nullable = false)
    private PriceType priceType;
    
    @Column(name = "normal_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal normalPrice;
    
    @Column(name = "supported_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal supportedPrice;
    
    @Column(name = "actual_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal actualPrice;
    
    @Column(name = "support_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal supportAmount = BigDecimal.ZERO;
    
    @Column(name = "employee_salary", precision = 10, scale = 2)
    private BigDecimal employeeSalary;
    
    @Column(name = "recorded_at")
    private LocalDateTime recordedAt;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (recordedAt == null) {
            recordedAt = LocalDateTime.now();
        }
    }
    
    public enum PriceType {
        NORMAL, SUPPORTED
    }
} 