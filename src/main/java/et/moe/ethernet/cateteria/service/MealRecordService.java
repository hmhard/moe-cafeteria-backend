package et.moe.ethernet.cateteria.service;

import et.moe.ethernet.cateteria.dto.MealRecordDto;
import et.moe.ethernet.cateteria.dto.EmployeeDto;
import et.moe.ethernet.cateteria.dto.MealCategoryDto;
import et.moe.ethernet.cateteria.entity.*;
import et.moe.ethernet.cateteria.repository.MealRecordRepository;
import et.moe.ethernet.cateteria.repository.SupportConfigRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MealRecordService {
    
    private final MealRecordRepository mealRecordRepository;
    private final EmployeeService employeeService;
    private final MealCategoryService mealCategoryService;
    private final SupportConfigRepository supportConfigRepository;
    private final UserService userService;
    
    public List<MealRecordDto> getAllMealRecords() {
        return mealRecordRepository.findAllOrderByRecordedAtDesc().stream()
            .map(MealRecordDto::fromEntity)
            .collect(Collectors.toList());
    }
    
    public List<MealRecordDto> getMealRecordsByEmployee(String employeeId) {
        return mealRecordRepository.findByEmployeeIdOrderByRecordedAtDesc(employeeId).stream()
            .map(MealRecordDto::fromEntity)
            .collect(Collectors.toList());
    }
    
    public List<MealRecordDto> getMealRecordsByDateRange(LocalDateTime start, LocalDateTime end) {
        return mealRecordRepository.findByRecordedAtBetween(start, end).stream()
            .map(MealRecordDto::fromEntity)
            .collect(Collectors.toList());
    }
    
    public List<MealRecordDto> getMealRecordsByDepartmentAndDateRange(String department, LocalDateTime start, LocalDateTime end) {
        return mealRecordRepository.findByDepartmentAndDateRange(department, start, end).stream()
            .map(MealRecordDto::fromEntity)
            .collect(Collectors.toList());
    }
    
    public Optional<MealRecordDto> getMealRecordById(String id) {
        return mealRecordRepository.findById(id)
            .map(MealRecordDto::fromEntity);
    }
    
    public boolean hasUsedMealTypeToday(String cardId, String mealTypeId) {
        LocalDate today = LocalDate.now();
        long existingMeals = mealRecordRepository.countByCardIdAndMealTypeIdAndDate(cardId, mealTypeId, today);
        return existingMeals > 0;
    }
    
    public MealRecordDto recordMeal(String cardId, String mealCategoryId) {
        // Find employee by card ID
        Employee employee = employeeService.getEmployeeByCardId(cardId)
            .map(this::mapToEmployeeEntity)
            .orElseThrow(() -> new RuntimeException("Employee not found or inactive"));
        
        // Find meal category
        MealCategory mealCategory = mealCategoryService.getMealCategoryById(mealCategoryId)
            .map(this::mapToMealCategoryEntity)
            .orElseThrow(() -> new RuntimeException("Meal category not found"));
        
        // Check if employee has already used this meal type today
        LocalDate today = LocalDate.now();
        long existingMeals = mealRecordRepository.countByCardIdAndMealTypeIdAndDate(
            cardId, mealCategory.getMealType().getId(), today);
        
        if (existingMeals > 0) {
            throw new RuntimeException("Employee has already used this meal type today");
        }
        
        // Calculate pricing
        MealPricing pricing = calculateMealPricing(employee, mealCategory);
        
        // Create meal record
        MealRecord mealRecord = new MealRecord();
        mealRecord.setEmployee(employee);
        mealRecord.setCardId(cardId);
        mealRecord.setMealType(mealCategory.getMealType());
        mealRecord.setMealCategory(mealCategory);
        mealRecord.setMealName(mealCategory.getName());
        mealRecord.setCategory(mealCategory.getCategory());
        mealRecord.setPriceType(pricing.priceType);
        mealRecord.setNormalPrice(pricing.normalPrice);
        mealRecord.setSupportedPrice(pricing.supportedPrice);
        mealRecord.setActualPrice(pricing.actualPrice);
        mealRecord.setSupportAmount(pricing.supportAmount);
        mealRecord.setEmployeeSalary(employee.getSalary());
        mealRecord.setRecordedAt(LocalDateTime.now());
        
        // Set the current user who recorded the meal
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            userService.findByUsername(username).ifPresent(mealRecord::setRecordedByUser);
        }
        
        MealRecord savedRecord = mealRecordRepository.save(mealRecord);
        return MealRecordDto.fromEntity(savedRecord);
    }
    
    private MealPricing calculateMealPricing(Employee employee, MealCategory mealCategory) {
        boolean eligibleForSupport = isEligibleForSupport(employee);
        
        BigDecimal normalPrice = mealCategory.getNormalPrice();
        BigDecimal supportedPrice = mealCategory.getSupportedPrice();
        BigDecimal actualPrice = eligibleForSupport ? supportedPrice : normalPrice;
        BigDecimal supportAmount = eligibleForSupport ? normalPrice.subtract(supportedPrice) : BigDecimal.ZERO;
        
        MealRecord.PriceType priceType = eligibleForSupport ? 
            MealRecord.PriceType.SUPPORTED : MealRecord.PriceType.NORMAL;
        
        return new MealPricing(normalPrice, supportedPrice, actualPrice, supportAmount, priceType);
    }
    
    private boolean isEligibleForSupport(Employee employee) {
        if (employee.getSalary() == null) {
            return false;
        }
        
        SupportConfig supportConfig = supportConfigRepository.findByIsActiveTrue()
            .orElse(new SupportConfig());
        
        return employee.getSalary().compareTo(supportConfig.getMaxSalaryForSupport()) < 0;
    }
    
    private Employee mapToEmployeeEntity(EmployeeDto employeeDto) {
        Employee employee = new Employee();
        employee.setId(employeeDto.getId());
        employee.setEmployeeId(employeeDto.getEmployeeId());
        employee.setCardId(employeeDto.getCardId());
        employee.setShortCode(employeeDto.getShortCode());
        employee.setName(employeeDto.getName());
        employee.setDepartment(employeeDto.getDepartment());
        employee.setSalary(employeeDto.getSalary());
        employee.setPhotoUrl(employeeDto.getPhotoUrl());
        employee.setActive(employeeDto.isActive());
        return employee;
    }
    
    private MealCategory mapToMealCategoryEntity(MealCategoryDto mealCategoryDto) {
        MealCategory mealCategory = new MealCategory();
        mealCategory.setId(mealCategoryDto.getId());
        mealCategory.setCategory(MealCategory.MealCategoryType.valueOf(mealCategoryDto.getCategory().toUpperCase()));
        mealCategory.setName(mealCategoryDto.getName());
        mealCategory.setNormalPrice(mealCategoryDto.getNormalPrice());
        mealCategory.setSupportedPrice(mealCategoryDto.getSupportedPrice());
        mealCategory.setActive(mealCategoryDto.isActive());
        
        // Create a minimal meal type reference
        MealType mealType = new MealType();
        mealType.setId(mealCategoryDto.getMealTypeId());
        mealCategory.setMealType(mealType);
        
        return mealCategory;
    }
    
    private static class MealPricing {
        final BigDecimal normalPrice;
        final BigDecimal supportedPrice;
        final BigDecimal actualPrice;
        final BigDecimal supportAmount;
        final MealRecord.PriceType priceType;
        
        MealPricing(BigDecimal normalPrice, BigDecimal supportedPrice, BigDecimal actualPrice, 
                   BigDecimal supportAmount, MealRecord.PriceType priceType) {
            this.normalPrice = normalPrice;
            this.supportedPrice = supportedPrice;
            this.actualPrice = actualPrice;
            this.supportAmount = supportAmount;
            this.priceType = priceType;
        }
    }
} 