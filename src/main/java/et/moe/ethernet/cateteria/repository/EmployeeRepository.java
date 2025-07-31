package et.moe.ethernet.cateteria.repository;

import et.moe.ethernet.cateteria.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, String> {
    
    Optional<Employee> findByEmployeeId(String employeeId);
    
    Optional<Employee> findByCardId(String cardId);
    
    Optional<Employee> findByShortCode(String shortCode);
    
    Optional<Employee> findByCardIdAndIsActiveTrue(String cardId);
    
    Optional<Employee> findByShortCodeAndIsActiveTrue(String shortCode);
    
    List<Employee> findByIsActiveTrue();
    
    List<Employee> findByDepartment(String department);
    
    List<Employee> findByDepartmentAndIsActiveTrue(String department);
    
    @Query("SELECT e FROM Employee e WHERE e.salary < ?1 AND e.isActive = true")
    List<Employee> findEligibleForSupport(BigDecimal maxSalary);
    
    boolean existsByCardId(String cardId);
    
    boolean existsByShortCode(String shortCode);
    
    boolean existsByEmployeeId(String employeeId);

    Optional<Employee> findByCardIdOrShortCode(String cardId, String shortCode);
    Optional<Employee> findByCardIdOrShortCodeAndIsActiveTrue(String cardId, String shortCode);
} 