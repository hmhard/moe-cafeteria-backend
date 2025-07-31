package et.moe.ethernet.cateteria.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import et.moe.ethernet.cateteria.entity.Employee;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDto {
    
    private String id;
    private String employeeId;
    private String cardId;
    private String shortCode;
    private String name;
    private String department;
    private BigDecimal salary;
    private String photoUrl;
    
    @JsonProperty("isActive")
    private boolean isActive;
    
    private boolean eligibleForSupport;
    private String supportStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public static EmployeeDto fromEntity(Employee employee, boolean eligibleForSupport) {
        return new EmployeeDto(
            employee.getId(),
            employee.getEmployeeId(),
            employee.getCardId(),
            employee.getShortCode(),
            employee.getName(),
            employee.getDepartment(),
            employee.getSalary(),
            employee.getPhotoUrl(),
            employee.isActive(),
            eligibleForSupport,
            eligibleForSupport ? "Eligible" : "Not Eligible",
            employee.getCreatedAt(),
            employee.getUpdatedAt()
        );
    }
} 