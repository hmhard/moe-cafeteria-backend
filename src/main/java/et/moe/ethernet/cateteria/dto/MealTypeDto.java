package et.moe.ethernet.cateteria.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import et.moe.ethernet.cateteria.entity.MealType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MealTypeDto {
    
    private String id;
    private String name;
    private String icon;
    private String color;
    
    @JsonProperty("isActive")
    private boolean isActive;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public static MealTypeDto fromEntity(MealType mealType) {
        return new MealTypeDto(
            mealType.getId(),
            mealType.getName(),
            mealType.getIcon().name().toLowerCase(),
            mealType.getColor(),
            mealType.isActive(),
            mealType.getCreatedAt(),
            mealType.getUpdatedAt()
        );
    }
} 