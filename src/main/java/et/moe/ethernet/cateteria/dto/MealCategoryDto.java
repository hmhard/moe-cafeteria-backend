package et.moe.ethernet.cateteria.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import et.moe.ethernet.cateteria.entity.MealCategory;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MealCategoryDto {
    
    private String id;
    private String mealTypeId;
    private String category;
    private String name;
    private BigDecimal normalPrice;
    private BigDecimal supportedPrice;
    
    @JsonProperty("allowedCount")
    private Integer allowedCount;
    
    @JsonProperty("isActive")
    private boolean isActive;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public static MealCategoryDto fromEntity(MealCategory mealCategory) {
        return new MealCategoryDto(
            mealCategory.getId(),
            mealCategory.getMealType().getId(),
            mealCategory.getCategory().name().toLowerCase(),
            mealCategory.getName(),
            mealCategory.getNormalPrice(),
            mealCategory.getSupportedPrice(),
            mealCategory.getAllowedCount(),
            mealCategory.isActive(),
            mealCategory.getCreatedAt(),
            mealCategory.getUpdatedAt()
        );
    }
} 