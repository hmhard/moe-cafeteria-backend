package et.moe.ethernet.cateteria.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateMealCategoryRequest {
    
    private String name;
    private String mealTypeId;
    private String category;
    private BigDecimal normalPrice;
    private BigDecimal supportedPrice;
    
    @JsonProperty("isActive")
    private boolean isActive = true;
} 