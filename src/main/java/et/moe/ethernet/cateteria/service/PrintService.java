package et.moe.ethernet.cateteria.service;

import et.moe.ethernet.cateteria.dto.MealRecordDto;
import et.moe.ethernet.cateteria.dto.EmployeeDto;
import et.moe.ethernet.cateteria.dto.MealCategoryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PrintService {
    
    private final EmployeeService employeeService;
    private final MealCategoryService mealCategoryService;
    
    public String generateReceiptText(MealRecordDto mealRecord) {
        StringBuilder receipt = new StringBuilder();
        
        // Get employee and meal category details
        EmployeeDto employee = employeeService.getEmployeeByCardId(mealRecord.getCardId())
            .orElseThrow(() -> new RuntimeException("Employee not found"));
        
        MealCategoryDto mealCategory = mealCategoryService.getMealCategoryById(mealRecord.getMealCategoryId())
            .orElseThrow(() -> new RuntimeException("Meal category not found"));
        
        // Format current time
        String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        String currentDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        
        // Generate simplified receipt content with only requested fields - ensure UTF-8 encoding
        receipt.append("MOE CAFETERIA\n");
        receipt.append("Order: ").append(mealRecord.getOrderNumber()).append("\n");
        receipt.append("Date: ").append(currentDate).append("\n");
        receipt.append("Time: ").append(currentTime).append("\n");
        receipt.append("Employee: ").append(employee.getShortCode()).append("\n");
        receipt.append("Meal Type: ").append(mealRecord.getMealTypeId()).append("\n");
        receipt.append("Meal Category: ").append(mealCategory.getName()).append("\n");
        receipt.append("Actual Price: ").append(String.format("%.2f", mealRecord.getActualPrice())).append(" ETB\n");
        receipt.append("Thank you for using our service!\n");
        
        return receipt.toString();
    }
    
    public String generateSimpleReceiptText(MealRecordDto mealRecord) {
        StringBuilder receipt = new StringBuilder();
        
        // Get employee and meal category details
        EmployeeDto employee = employeeService.getEmployeeByCardId(mealRecord.getCardId())
            .orElseThrow(() -> new RuntimeException("Employee not found"));
        
        MealCategoryDto mealCategory = mealCategoryService.getMealCategoryById(mealRecord.getMealCategoryId())
            .orElseThrow(() -> new RuntimeException("Meal category not found"));
        
        // Format current time
        String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        String currentDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        
        // Generate simplified receipt content with only requested fields - ensure UTF-8 encoding
        receipt.append("MOE CAFETERIA\n");
        receipt.append("Order: ").append(mealRecord.getOrderNumber()).append("\n");
        receipt.append("Date: ").append(currentDate).append("\n");
        receipt.append("Time: ").append(currentTime).append("\n");
        receipt.append("Employee: ").append(employee.getShortCode()).append("\n");
        receipt.append("Meal Type: ").append(mealRecord.getMealTypeId()).append("\n");
        receipt.append("Meal Category: ").append(mealCategory.getName()).append("\n");
        receipt.append("Actual Price: ").append(String.format("%.2f", mealRecord.getActualPrice())).append(" ETB\n");
        receipt.append("Thank you for using our service!\n");
        
        return receipt.toString();
    }
} 