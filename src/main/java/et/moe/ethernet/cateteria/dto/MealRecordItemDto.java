package et.moe.ethernet.cateteria.dto;

import et.moe.ethernet.cateteria.entity.MealRecordItem;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MealRecordItemDto {
    
    private String id;
    private String mealRecordId;
    private String mealItemId;
    private String mealItemName;
    private Integer quantity;
    private BigDecimal pricePerItem;
    private BigDecimal totalPrice;
    private LocalDateTime createdAt;
    
    public static MealRecordItemDto fromEntity(MealRecordItem entity) {
        MealRecordItemDto dto = new MealRecordItemDto();
        dto.setId(entity.getId());
        dto.setMealRecordId(entity.getMealRecord().getId());
        dto.setMealItemId(entity.getMealItem().getId());
        dto.setMealItemName(entity.getMealItem().getName());
        dto.setQuantity(entity.getQuantity());
        dto.setPricePerItem(entity.getPricePerItem());
        dto.setTotalPrice(entity.getTotalPrice());
        dto.setCreatedAt(entity.getCreatedAt());
        return dto;
    }
} 