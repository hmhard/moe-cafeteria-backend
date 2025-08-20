package et.moe.ethernet.cateteria.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import et.moe.ethernet.cateteria.entity.MealItem;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MealItemDto {
    
    private String id;
    private String mealCategoryId;
    private String name;
    private String description;
    private String imageUrl;
    private String color;
    
    @JsonProperty("totalAvailable")
    private Integer totalAvailable;
    
    @JsonProperty("isActive")
    private boolean isActive;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public static MealItemDto fromEntity(MealItem mealItem) {
        return new MealItemDto(
            mealItem.getId(),
            mealItem.getMealCategory().getId(),
            mealItem.getName(),
            mealItem.getDescription(),
            mealItem.getImageUrl(),
            mealItem.getColor(),
            mealItem.getTotalAvailable(),
            mealItem.isActive(),
            mealItem.getCreatedAt(),
            mealItem.getUpdatedAt()
        );
    }
} 