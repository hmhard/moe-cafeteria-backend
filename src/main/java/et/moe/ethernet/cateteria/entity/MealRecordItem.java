package et.moe.ethernet.cateteria.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "meal_record_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MealRecordItem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meal_record_id", nullable = false)
    private MealRecord mealRecord;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meal_item_id", nullable = false)
    private MealItem mealItem;
    
    @Column(nullable = false)
    private Integer quantity = 1;
    
    @Column(name = "price_per_item", nullable = false, precision = 10, scale = 2)
    private BigDecimal pricePerItem;
    
    @Column(name = "total_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPrice;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
} 