package et.moe.ethernet.cateteria.config;

import et.moe.ethernet.cateteria.entity.*;
import et.moe.ethernet.cateteria.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {
    
    private final MealTypeRepository mealTypeRepository;
    private final MealCategoryRepository mealCategoryRepository;
    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;
    private final SupportConfigRepository supportConfigRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Override
    public void run(String... args) throws Exception {
        log.info("Initializing database with sample data...");
        
        // Initialize meal types
        // initializeMealTypes();
        
        // Initialize meal categories
        // initializeMealCategories();
        
        // Initialize employees
        initializeEmployees();
        
        // Initialize users
        initializeUsers();
        
        // Initialize support config
        initializeSupportConfig();
        
        log.info("Database initialization completed!");
    }
    
    private void initializeMealTypes() {
        if (mealTypeRepository.count() == 0) {
            MealType breakfast = new MealType();
            breakfast.setId("breakfast");
            breakfast.setName("ቁርስ");
            breakfast.setIcon(MealType.MealIcon.COFFEE);
            breakfast.setActive(true);
            breakfast.setColor("bg-amber-100 text-amber-700");
            mealTypeRepository.save(breakfast);
            
            MealType lunch = new MealType();
            lunch.setId("lunch");
            lunch.setName("ምሳ");
            lunch.setIcon(MealType.MealIcon.UTENSILS);
            lunch.setActive(true);
            lunch.setColor("bg-emerald-100 text-emerald-700");
            mealTypeRepository.save(lunch);
            
            log.info("Meal types initialized");
        }
    }
    
    private void initializeMealCategories() {
        if (mealCategoryRepository.count() == 0) {
            MealType breakfast = mealTypeRepository.findById("breakfast").orElseThrow();
            MealType lunch = mealTypeRepository.findById("lunch").orElseThrow();
            
            // Breakfast categories
            MealCategory breakfastFasting = new MealCategory();
            breakfastFasting.setMealType(breakfast);
            breakfastFasting.setCategory(MealCategory.MealCategoryType.FASTING);
            breakfastFasting.setName("ቁርስ - ጾም");
            breakfastFasting.setNormalPrice(new BigDecimal("30.00"));
            breakfastFasting.setSupportedPrice(new BigDecimal("20.00"));
            breakfastFasting.setActive(true);
            mealCategoryRepository.save(breakfastFasting);
            
            MealCategory breakfastNonFasting = new MealCategory();
            breakfastNonFasting.setMealType(breakfast);
            breakfastNonFasting.setCategory(MealCategory.MealCategoryType.NON_FASTING);
            breakfastNonFasting.setName("ቁርስ - የፍስግ");
            breakfastNonFasting.setNormalPrice(new BigDecimal("40.00"));
            breakfastNonFasting.setSupportedPrice(new BigDecimal("30.00"));
            breakfastNonFasting.setActive(true);
            mealCategoryRepository.save(breakfastNonFasting);
            
            // Lunch categories
            MealCategory lunchFasting = new MealCategory();
            lunchFasting.setMealType(lunch);
            lunchFasting.setCategory(MealCategory.MealCategoryType.FASTING);
            lunchFasting.setName("ምሳ - ጾም");
            lunchFasting.setNormalPrice(new BigDecimal("50.00"));
            lunchFasting.setSupportedPrice(new BigDecimal("40.00"));
            lunchFasting.setActive(true);
            mealCategoryRepository.save(lunchFasting);
            
            MealCategory lunchNonFasting = new MealCategory();
            lunchNonFasting.setMealType(lunch);
            lunchNonFasting.setCategory(MealCategory.MealCategoryType.NON_FASTING);
            lunchNonFasting.setName("ምሳ - የፍስግ");
            lunchNonFasting.setNormalPrice(new BigDecimal("60.00"));
            lunchNonFasting.setSupportedPrice(new BigDecimal("50.00"));
            lunchNonFasting.setActive(true);
            mealCategoryRepository.save(lunchNonFasting);
            
            log.info("Meal categories initialized");
        }
    }
    
    private void initializeEmployees() {
        if (employeeRepository.count() == 0) {
            Employee emp1 = new Employee();
            emp1.setEmployeeId("EMP001");
            emp1.setCardId("04A2B3C4D5");
            emp1.setShortCode("1234");
            emp1.setName("አበበ ቢቂላ");
            emp1.setDepartment("ኢንጂነሪንግ");
            emp1.setSalary(new BigDecimal("4500.00"));
            emp1.setPhotoUrl("/placeholder.svg?height=200&width=200&text=አበ");
            emp1.setActive(true);
            employeeRepository.save(emp1);
            
            Employee emp2 = new Employee();
            emp2.setEmployeeId("EMP002");
            emp2.setCardId("15F6E7D8C9");
            emp2.setShortCode("5678");
            emp2.setName("ሰላም ታደሰ");
            emp2.setDepartment("ማርኬቲንግ");
            emp2.setSalary(new BigDecimal("3800.00"));
            emp2.setPhotoUrl("/placeholder.svg?height=200&width=200&text=ሰላ");
            emp2.setActive(true);
            employeeRepository.save(emp2);
            
            Employee emp3 = new Employee();
            emp3.setEmployeeId("EMP003");
            emp3.setCardId("26A7B8C9D0");
            emp3.setShortCode("9012");
            emp3.setName("ፍቃዱ ተስፋዬ");
            emp3.setDepartment("ፋይናንስ");
            emp3.setSalary(new BigDecimal("4200.00"));
            emp3.setPhotoUrl("/placeholder.svg?height=200&width=200&text=ፍቃ");
            emp3.setActive(false);
            employeeRepository.save(emp3);
            
            Employee emp4 = new Employee();
            emp4.setEmployeeId("EMP004");
            emp4.setCardId("37B8C9D0E1");
            emp4.setShortCode("3456");
            emp4.setName("ሄኖክ መንግስቱ");
            emp4.setDepartment("ሰው ሃይል");
            emp4.setSalary(new BigDecimal("6500.00"));
            emp4.setPhotoUrl("/placeholder.svg?height=200&width=200&text=ሄኖ");
            emp4.setActive(true);
            employeeRepository.save(emp4);
            
            Employee emp5 = new Employee();
            emp5.setEmployeeId("EMP005");
            emp5.setCardId("48C9D0E1F2");
            emp5.setShortCode("7890");
            emp5.setName("ብርሃን አለሙ");
            emp5.setDepartment("ኢንፎርሜሽን ቴክኖሎጂ");
            emp5.setSalary(new BigDecimal("7200.00"));
            emp5.setPhotoUrl("/placeholder.svg?height=200&width=200&text=ብር");
            emp5.setActive(true);
            employeeRepository.save(emp5);
            
            log.info("Employees initialized");
        }
    }
    
    private void initializeUsers() {
        if (userRepository.count() == 0) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setEmail("admin@moe.gov.et");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setFullName("System Administrator");
            admin.setRole(User.UserRole.ADMIN);
            admin.setActive(true);
            admin.setCreatedAt(LocalDateTime.now());
            admin.setUpdatedAt(LocalDateTime.now());
            userRepository.save(admin);
            
            User manager = new User();
            manager.setUsername("manager");
            manager.setEmail("manager@moe.gov.et");
            manager.setPassword(passwordEncoder.encode("manager123"));
            manager.setFullName("Cafeteria Manager");
            manager.setRole(User.UserRole.MANAGER);
            manager.setActive(true);
            manager.setCreatedAt(LocalDateTime.now());
            manager.setUpdatedAt(LocalDateTime.now());
            userRepository.save(manager);
            
            User operator = new User();
            operator.setUsername("operator");
            operator.setEmail("operator@moe.gov.et");
            operator.setPassword(passwordEncoder.encode("operator123"));
            operator.setFullName("Cafeteria Operator");
            operator.setRole(User.UserRole.OPERATOR);
            operator.setActive(true);
            operator.setCreatedAt(LocalDateTime.now());
            operator.setUpdatedAt(LocalDateTime.now());
            userRepository.save(operator);
            
            log.info("Users initialized");
        }
    }
    
    private void initializeSupportConfig() {
        if (supportConfigRepository.count() == 0) {
            SupportConfig config = new SupportConfig();
            config.setMaxSalaryForSupport(new BigDecimal("5000.00"));
            config.setActive(true);
            config.setCreatedAt(LocalDateTime.now());
            config.setUpdatedAt(LocalDateTime.now());
            supportConfigRepository.save(config);
            
            log.info("Support configuration initialized");
        }
    }
} 