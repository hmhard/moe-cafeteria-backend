package et.moe.ethernet.cateteria.service;

import et.moe.ethernet.cateteria.dto.EmployeeDto;
import et.moe.ethernet.cateteria.dto.EmployeeUsageStatsDto;
import et.moe.ethernet.cateteria.dto.MealRecordDto;
import et.moe.ethernet.cateteria.entity.Employee;
import et.moe.ethernet.cateteria.entity.SupportConfig;
import et.moe.ethernet.cateteria.repository.EmployeeRepository;
import et.moe.ethernet.cateteria.repository.MealRecordRepository;
import et.moe.ethernet.cateteria.repository.SupportConfigRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    
    private final EmployeeRepository employeeRepository;
    private final SupportConfigRepository supportConfigRepository;
    private final MealRecordRepository mealRecordRepository;
    
    public List<EmployeeDto> getAllEmployees() {
        return employeeRepository.findByIsActiveTrue().stream()
            .map(employee -> EmployeeDto.fromEntity(employee, isEligibleForSupport(employee)))
            .collect(Collectors.toList());
    }
    
    public List<EmployeeDto> getEmployeesByDepartment(String department) {
        return employeeRepository.findByDepartmentAndIsActiveTrue(department).stream()
            .map(employee -> EmployeeDto.fromEntity(employee, isEligibleForSupport(employee)))
            .collect(Collectors.toList());
    }
    
    public Optional<EmployeeDto> getEmployeeById(String id) {
        return employeeRepository.findById(id)
            .map(employee -> EmployeeDto.fromEntity(employee, isEligibleForSupport(employee)));
    }
    
    public Optional<EmployeeDto> getEmployeeByEmployeeId(String employeeId) {
        return employeeRepository.findByEmployeeId(employeeId)
            .map(employee -> EmployeeDto.fromEntity(employee, isEligibleForSupport(employee)));
    }
    
    public Optional<EmployeeDto> getEmployeeByCardId(String cardId) {
        return employeeRepository.findByCardIdOrShortCodeAndIsActiveTrue(cardId, cardId)
            .map(employee -> EmployeeDto.fromEntity(employee, isEligibleForSupport(employee)));
    }
    
    public Optional<EmployeeDto> getEmployeeByShortCode(String shortCode) {
        return employeeRepository.findByShortCodeAndIsActiveTrue(shortCode)
            .map(employee -> EmployeeDto.fromEntity(employee, isEligibleForSupport(employee)));
    }
    
    public EmployeeDto createEmployee(Employee employee) {
        // Validate unique constraints
        if (employeeRepository.existsByEmployeeId(employee.getEmployeeId())) {
            throw new RuntimeException("Employee ID already exists");
        }
        if (employee.getCardId() != null && employeeRepository.existsByCardId(employee.getCardId())) {
            throw new RuntimeException("Card ID already assigned");
        }
        if (employee.getShortCode() != null && employeeRepository.existsByShortCode(employee.getShortCode())) {
            throw new RuntimeException("Short code already assigned");
        }
        
        Employee savedEmployee = employeeRepository.save(employee);
        return EmployeeDto.fromEntity(savedEmployee, isEligibleForSupport(savedEmployee));
    }
    
    public Optional<EmployeeDto> updateEmployee(String id, Employee employeeUpdates) {
        return employeeRepository.findById(id)
            .map(existingEmployee -> {
                // Check for unique constraint violations
                if (!existingEmployee.getEmployeeId().equals(employeeUpdates.getEmployeeId()) &&
                    employeeRepository.existsByEmployeeId(employeeUpdates.getEmployeeId())) {
                    throw new RuntimeException("Employee ID already exists");
                }
                if (employeeUpdates.getCardId() != null && 
                    !employeeUpdates.getCardId().equals(existingEmployee.getCardId()) &&
                    employeeRepository.existsByCardId(employeeUpdates.getCardId())) {
                    throw new RuntimeException("Card ID already assigned");
                }
                if (employeeUpdates.getShortCode() != null && 
                    !employeeUpdates.getShortCode().equals(existingEmployee.getShortCode()) &&
                    employeeRepository.existsByShortCode(employeeUpdates.getShortCode())) {
                    throw new RuntimeException("Short code already assigned");
                }
                
                existingEmployee.setEmployeeId(employeeUpdates.getEmployeeId());
                existingEmployee.setCardId(employeeUpdates.getCardId());
                existingEmployee.setShortCode(employeeUpdates.getShortCode());
                existingEmployee.setName(employeeUpdates.getName());
                existingEmployee.setDepartment(employeeUpdates.getDepartment());
                existingEmployee.setSalary(employeeUpdates.getSalary());
                existingEmployee.setPhotoUrl(employeeUpdates.getPhotoUrl());
                existingEmployee.setActive(employeeUpdates.isActive());
                
                Employee savedEmployee = employeeRepository.save(existingEmployee);
                return EmployeeDto.fromEntity(savedEmployee, isEligibleForSupport(savedEmployee));
            });
    }
    
    public boolean toggleEmployeeStatus(String id) {
        return employeeRepository.findById(id)
            .map(employee -> {
                employee.setActive(!employee.isActive());
                employeeRepository.save(employee);
                return true;
            })
            .orElse(false);
    }
    
    public boolean deleteEmployee(String id) {
        if (employeeRepository.existsById(id)) {
            employeeRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    public boolean isEligibleForSupport(Employee employee) {
        if (employee.getSalary() == null) {
            return false;
        }
        
        SupportConfig supportConfig = supportConfigRepository.findByIsActiveTrue()
            .orElse(new SupportConfig());
        
        return employee.getSalary().compareTo(supportConfig.getMaxSalaryForSupport()) < 0;
    }
    
    public List<EmployeeDto> getEligibleEmployeesForSupport() {
        return employeeRepository.findByIsActiveTrue().stream()
            .filter(this::isEligibleForSupport)
            .map(employee -> EmployeeDto.fromEntity(employee, true))
            .collect(Collectors.toList());
    }
    
    public Optional<EmployeeUsageStatsDto> getEmployeeUsageStats(String employeeId) {
        return employeeRepository.findByEmployeeId(employeeId)
            .map(employee -> {
                // Get all meal records for this employee
                var mealRecordEntities = mealRecordRepository.findByEmployeeIdOrderByRecordedAtDesc(employeeId);
                var mealRecords = mealRecordEntities.stream()
                    .map(record -> et.moe.ethernet.cateteria.dto.MealRecordDto.fromEntity(record))
                    .collect(Collectors.toList());
                
                int totalMeals = mealRecords.size();
                BigDecimal totalAmount = mealRecords.stream()
                    .map(record -> record.getActualPrice())
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
                BigDecimal totalSubsidy = mealRecords.stream()
                    .map(record -> record.getSupportAmount())
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
                BigDecimal totalSavings = totalSubsidy; // Same as totalSubsidy
                
                // Count meals by type
                Map<String, Integer> mealCounts = mealRecords.stream()
                    .collect(Collectors.groupingBy(
                        record -> record.getMealTypeId(),
                        Collectors.collectingAndThen(Collectors.counting(), Long::intValue)
                    ));
                
                // Sum amounts by meal type
                Map<String, BigDecimal> mealAmounts = mealRecords.stream()
                    .collect(Collectors.groupingBy(
                        record -> record.getMealTypeId(),
                        Collectors.mapping(
                            record -> record.getActualPrice(),
                            Collectors.reducing(BigDecimal.ZERO, BigDecimal::add)
                        )
                    ));
                
                int supportedMeals = (int) mealRecords.stream()
                    .filter(record -> "supported".equals(record.getPriceType()))
                    .count();
                int normalMeals = totalMeals - supportedMeals;
                
                return new EmployeeUsageStatsDto(
                    totalMeals,
                    totalAmount,
                    totalSubsidy,
                    totalSavings,
                    mealCounts,
                    mealAmounts,
                    supportedMeals,
                    normalMeals
                );
            });
    }
    
    public Optional<List<MealRecordDto>> getEmployeeMealRecords(String employeeId) {
        return employeeRepository.findByEmployeeId(employeeId)
            .map(employee -> {
                var mealRecordEntities = mealRecordRepository.findByEmployeeIdOrderByRecordedAtDesc(employeeId);
                return mealRecordEntities.stream()
                    .map(record -> et.moe.ethernet.cateteria.dto.MealRecordDto.fromEntity(record))
                    .collect(Collectors.toList());
            });
    }
} 