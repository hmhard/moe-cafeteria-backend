package et.moe.ethernet.cateteria.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateMealItemRequest {
    
    private String name;
    private String description;
    private String mealCategoryId;
    private String imageUrl;
    private String color;
    
    @JsonProperty("totalAvailable")
    private Integer totalAvailable = 0;
    
    @JsonProperty("isActive")
    private boolean isActive = true;
} 