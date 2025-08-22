package et.moe.ethernet.cateteria.dto;

import et.moe.ethernet.cateteria.entity.MealRecord;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MealRecordDto {
    
    private String id;
    private String employeeId;
    private String cardId;
    private String mealTypeId;
    private String mealCategoryId;
    private String mealName;
    private String category;
    private String priceType;
    private BigDecimal normalPrice;
    private BigDecimal supportedPrice;
    private BigDecimal actualPrice;
    private BigDecimal supportAmount;
    private BigDecimal employeeSalary;
    private LocalDateTime timestamp;
    private LocalDateTime createdAt;
    private String orderNumber;
    
    // User information
    private String recordedByUserId;
    private String recordedByUsername;
    private String recordedByFullName;
    
    // Meal items information
    private List<MealRecordItemDto> mealItems;
    
    public static MealRecordDto fromEntity(MealRecord mealRecord) {
        return new MealRecordDto(
            mealRecord.getId(),
            mealRecord.getEmployee().getEmployeeId(),
            mealRecord.getCardId(),
            mealRecord.getMealType().getId(),
            mealRecord.getMealCategory().getId(),
            mealRecord.getMealName(),
            mealRecord.getCategory().name().toLowerCase(),
            mealRecord.getPriceType().name().toLowerCase(),
            mealRecord.getNormalPrice(),
            mealRecord.getSupportedPrice(),
            mealRecord.getActualPrice(),
            mealRecord.getSupportAmount(),
            mealRecord.getEmployeeSalary(),
            mealRecord.getRecordedAt(),
            mealRecord.getCreatedAt(),
            mealRecord.getOrderNumber(),
            mealRecord.getRecordedByUser() != null ? mealRecord.getRecordedByUser().getId() : null,
            mealRecord.getRecordedByUser() != null ? mealRecord.getRecordedByUser().getUsername() : null,
            mealRecord.getRecordedByUser() != null ? mealRecord.getRecordedByUser().getFullName() : null,
            null // mealItems will be set separately
        );
    }
} 