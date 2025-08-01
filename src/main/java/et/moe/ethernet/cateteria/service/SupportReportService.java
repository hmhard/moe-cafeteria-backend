package et.moe.ethernet.cateteria.service;

import et.moe.ethernet.cateteria.entity.*;
import et.moe.ethernet.cateteria.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Service
@RequiredArgsConstructor
public class SupportReportService {
    
    private final MealRecordRepository mealRecordRepository;
    private final EmployeeRepository employeeRepository;
    private final SupportConfigRepository supportConfigRepository;
    
    public SupportSummary getSupportSummary(String period) {
        LocalDateTime startDate = getStartDateForPeriod(period);
        LocalDateTime endDate = LocalDateTime.now();
        
        List<MealRecord> records = mealRecordRepository.findByRecordedAtBetween(startDate, endDate);
        List<Employee> employees = employeeRepository.findByIsActiveTrue();
        
        // Calculate totals
        long totalMeals = records.size();
        long supportedMeals = records.stream()
            .filter(record -> record.getPriceType() == MealRecord.PriceType.SUPPORTED)
            .count();
        long normalMeals = totalMeals - supportedMeals;
        
        BigDecimal totalRevenue = records.stream()
            .map(MealRecord::getActualPrice)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        BigDecimal totalSubsidy = records.stream()
            .map(MealRecord::getSupportAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        BigDecimal potentialRevenue = records.stream()
            .map(MealRecord::getNormalPrice)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        // Calculate employee statistics
        long totalEmployees = employees.size();
        long supportedEmployees = employees.stream()
            .filter(employee -> employee.getSalary() != null && isEligibleForSupport(employee))
            .count();
        
        double supportPercentage = totalMeals > 0 ? (double) supportedMeals / totalMeals * 100 : 0;
        
        return new SupportSummary(
            (int) totalMeals,
            (int) supportedMeals,
            (int) normalMeals,
            totalRevenue.doubleValue(),
            totalSubsidy.doubleValue(),
            potentialRevenue.doubleValue(),
            (int) supportedEmployees,
            (int) totalEmployees,
            supportPercentage
        );
    }
    
    public List<DepartmentSupportAnalysis> getDepartmentAnalysis(String period) {
        return getDepartmentAnalysis(period, 0, Integer.MAX_VALUE);
    }
    
    public List<DepartmentSupportAnalysis> getDepartmentAnalysis(String period, int page, int size) {
        LocalDateTime startDate = getStartDateForPeriod(period);
        LocalDateTime endDate = LocalDateTime.now();
        
        List<MealRecord> records = mealRecordRepository.findByRecordedAtBetween(startDate, endDate);
        
        // Get all employees with pagination
        Pageable pageable = PageRequest.of(page, size);
        Page<Employee> employeePage = employeeRepository.findByIsActiveTrue(pageable);
        List<Employee> employees = employeePage.getContent();
        
        // Group employees by department
        Map<String, List<Employee>> employeesByDepartment = employees.stream()
            .collect(Collectors.groupingBy(Employee::getDepartment));
        
        // Group records by department
        Map<String, List<MealRecord>> recordsByDepartment = records.stream()
            .collect(Collectors.groupingBy(record -> record.getEmployee().getDepartment()));
        
        return employeesByDepartment.entrySet().stream()
            .map(entry -> {
                String department = entry.getKey();
                List<Employee> deptEmployees = entry.getValue();
                List<MealRecord> deptRecords = recordsByDepartment.getOrDefault(department, List.of());
                
                return createDepartmentAnalysis(department, deptEmployees, deptRecords);
            })
            .collect(Collectors.toList());
    }
    
    public PaginatedDepartmentAnalysis getPaginatedDepartmentAnalysis(String period, int page, int size) {
        LocalDateTime startDate = getStartDateForPeriod(period);
        LocalDateTime endDate = LocalDateTime.now();
        
        List<MealRecord> records = mealRecordRepository.findByRecordedAtBetween(startDate, endDate);
        
        // Get all employees with pagination
        Pageable pageable = PageRequest.of(page, size);
        Page<Employee> employeePage = employeeRepository.findByIsActiveTrue(pageable);
        List<Employee> employees = employeePage.getContent();
        
        // Group employees by department
        Map<String, List<Employee>> employeesByDepartment = employees.stream()
            .collect(Collectors.groupingBy(Employee::getDepartment));
        
        // Group records by department
        Map<String, List<MealRecord>> recordsByDepartment = records.stream()
            .collect(Collectors.groupingBy(record -> record.getEmployee().getDepartment()));
        
        List<DepartmentSupportAnalysis> analysis = employeesByDepartment.entrySet().stream()
            .map(entry -> {
                String department = entry.getKey();
                List<Employee> deptEmployees = entry.getValue();
                List<MealRecord> deptRecords = recordsByDepartment.getOrDefault(department, List.of());
                
                return createDepartmentAnalysis(department, deptEmployees, deptRecords);
            })
            .collect(Collectors.toList());
        
        return new PaginatedDepartmentAnalysis(
            analysis,
            employeePage.getTotalElements(),
            employeePage.getTotalPages(),
            employeePage.getNumber(),
            employeePage.getSize()
        );
    }
    
    private DepartmentSupportAnalysis createDepartmentAnalysis(String department, List<Employee> employees, List<MealRecord> records) {
        int totalEmployees = employees.size();
        int eligibleEmployees = (int) employees.stream()
            .filter(employee -> employee.getSalary() != null && isEligibleForSupport(employee))
            .count();
        
        // Count employees using support (have at least one supported meal)
        int employeesUsingSupport = (int) employees.stream()
            .filter(employee -> records.stream()
                .anyMatch(record -> record.getEmployee().getId().equals(employee.getId()) && 
                         record.getPriceType() == MealRecord.PriceType.SUPPORTED))
            .count();
        
        int totalMeals = records.size();
        int supportedMeals = (int) records.stream()
            .filter(record -> record.getPriceType() == MealRecord.PriceType.SUPPORTED)
            .count();
        
        BigDecimal totalRevenue = records.stream()
            .map(MealRecord::getActualPrice)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        BigDecimal totalSubsidy = records.stream()
            .map(MealRecord::getSupportAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        // Calculate average department salary
        BigDecimal avgDepartmentSalary = employees.stream()
            .filter(employee -> employee.getSalary() != null)
            .map(Employee::getSalary)
            .reduce(BigDecimal.ZERO, BigDecimal::add)
            .divide(BigDecimal.valueOf(employees.stream().filter(e -> e.getSalary() != null).count()), 2, BigDecimal.ROUND_HALF_UP);
        
        double eligibilityPercentage = totalEmployees > 0 ? (double) eligibleEmployees / totalEmployees * 100 : 0;
        
        return new DepartmentSupportAnalysis(
            department,
            totalEmployees,
            eligibleEmployees,
            employeesUsingSupport,
            totalMeals,
            supportedMeals,
            totalRevenue.doubleValue(),
            totalSubsidy.doubleValue(),
            avgDepartmentSalary.doubleValue(),
            eligibilityPercentage
        );
    }
    
    private LocalDateTime getStartDateForPeriod(String period) {
        LocalDateTime now = LocalDateTime.now();
        return switch (period.toLowerCase()) {
            case "daily" -> now.toLocalDate().atStartOfDay();
            case "weekly" -> now.minus(7, ChronoUnit.DAYS);
            case "monthly" -> now.minus(30, ChronoUnit.DAYS);
            case "yearly" -> now.minus(365, ChronoUnit.DAYS);
            default -> now.minus(30, ChronoUnit.DAYS); // Default to monthly
        };
    }
    
    private boolean isEligibleForSupport(Employee employee) {
        if (employee.getSalary() == null) {
            return false;
        }
        
        SupportConfig supportConfig = supportConfigRepository.findByIsActiveTrue()
            .orElse(new SupportConfig());
        
        return employee.getSalary().compareTo(supportConfig.getMaxSalaryForSupport()) < 0;
    }
    
    // DTO classes for the response
    public static class SupportSummary {
        private final int totalMeals;
        private final int supportedMeals;
        private final int normalMeals;
        private final double totalRevenue;
        private final double totalSubsidy;
        private final double potentialRevenue;
        private final int supportedEmployees;
        private final int totalEmployees;
        private final double supportPercentage;
        
        public SupportSummary(int totalMeals, int supportedMeals, int normalMeals, double totalRevenue, 
                            double totalSubsidy, double potentialRevenue, int supportedEmployees, 
                            int totalEmployees, double supportPercentage) {
            this.totalMeals = totalMeals;
            this.supportedMeals = supportedMeals;
            this.normalMeals = normalMeals;
            this.totalRevenue = totalRevenue;
            this.totalSubsidy = totalSubsidy;
            this.potentialRevenue = potentialRevenue;
            this.supportedEmployees = supportedEmployees;
            this.totalEmployees = totalEmployees;
            this.supportPercentage = supportPercentage;
        }
        
        // Getters
        public int getTotalMeals() { return totalMeals; }
        public int getSupportedMeals() { return supportedMeals; }
        public int getNormalMeals() { return normalMeals; }
        public double getTotalRevenue() { return totalRevenue; }
        public double getTotalSubsidy() { return totalSubsidy; }
        public double getPotentialRevenue() { return potentialRevenue; }
        public int getSupportedEmployees() { return supportedEmployees; }
        public int getTotalEmployees() { return totalEmployees; }
        public double getSupportPercentage() { return supportPercentage; }
    }
    
    public static class DepartmentSupportAnalysis {
        private final String department;
        private final int totalEmployees;
        private final int eligibleEmployees;
        private final int employeesUsingSupport;
        private final int totalMeals;
        private final int supportedMeals;
        private final double totalRevenue;
        private final double totalSubsidy;
        private final double avgDepartmentSalary;
        private final double eligibilityPercentage;
        
        public DepartmentSupportAnalysis(String department, int totalEmployees, int eligibleEmployees, 
                                       int employeesUsingSupport, int totalMeals, int supportedMeals, 
                                       double totalRevenue, double totalSubsidy, double avgDepartmentSalary, 
                                       double eligibilityPercentage) {
            this.department = department;
            this.totalEmployees = totalEmployees;
            this.eligibleEmployees = eligibleEmployees;
            this.employeesUsingSupport = employeesUsingSupport;
            this.totalMeals = totalMeals;
            this.supportedMeals = supportedMeals;
            this.totalRevenue = totalRevenue;
            this.totalSubsidy = totalSubsidy;
            this.avgDepartmentSalary = avgDepartmentSalary;
            this.eligibilityPercentage = eligibilityPercentage;
        }
        
        // Getters
        public String getDepartment() { return department; }
        public int getTotalEmployees() { return totalEmployees; }
        public int getEligibleEmployees() { return eligibleEmployees; }
        public int getEmployeesUsingSupport() { return employeesUsingSupport; }
        public int getTotalMeals() { return totalMeals; }
        public int getSupportedMeals() { return supportedMeals; }
        public double getTotalRevenue() { return totalRevenue; }
        public double getTotalSubsidy() { return totalSubsidy; }
        public double getAvgDepartmentSalary() { return avgDepartmentSalary; }
        public double getEligibilityPercentage() { return eligibilityPercentage; }
    }
    
    public static class PaginatedDepartmentAnalysis {
        private final List<DepartmentSupportAnalysis> content;
        private final long totalElements;
        private final int totalPages;
        private final int currentPage;
        private final int pageSize;
        
        public PaginatedDepartmentAnalysis(List<DepartmentSupportAnalysis> content, long totalElements, 
                                         int totalPages, int currentPage, int pageSize) {
            this.content = content;
            this.totalElements = totalElements;
            this.totalPages = totalPages;
            this.currentPage = currentPage;
            this.pageSize = pageSize;
        }
        
        // Getters
        public List<DepartmentSupportAnalysis> getContent() { return content; }
        public long getTotalElements() { return totalElements; }
        public int getTotalPages() { return totalPages; }
        public int getCurrentPage() { return currentPage; }
        public int getPageSize() { return pageSize; }
    }
} 