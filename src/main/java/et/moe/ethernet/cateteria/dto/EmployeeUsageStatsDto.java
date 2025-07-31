package et.moe.ethernet.cateteria.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeUsageStatsDto {
    
    private int totalMeals;
    private BigDecimal totalAmount;
    private BigDecimal totalSubsidy;
    private BigDecimal totalSavings;
    private Map<String, Integer> mealCounts;
    private Map<String, BigDecimal> mealAmounts;
    private int supportedMeals;
    private int normalMeals;
} 