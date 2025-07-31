package et.moe.ethernet.cateteria.repository;

import et.moe.ethernet.cateteria.entity.MealRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MealRecordRepository extends JpaRepository<MealRecord, String> {
    
    List<MealRecord> findByEmployeeEmployeeId(String employeeId);
    
    List<MealRecord> findByCardId(String cardId);
    
    List<MealRecord> findByMealTypeId(String mealTypeId);
    
    List<MealRecord> findByMealCategoryId(String mealCategoryId);
    
    @Query("SELECT mr FROM MealRecord mr WHERE mr.cardId = ?1 AND mr.mealType.id = ?2 AND DATE(mr.recordedAt) = ?3")
    List<MealRecord> findByCardIdAndMealTypeIdAndDate(String cardId, String mealTypeId, LocalDate date);
    
    @Query("SELECT mr FROM MealRecord mr WHERE mr.recordedAt BETWEEN ?1 AND ?2")
    List<MealRecord> findByRecordedAtBetween(LocalDateTime start, LocalDateTime end);
    
    @Query("SELECT mr FROM MealRecord mr WHERE mr.employee.department = ?1 AND mr.recordedAt BETWEEN ?2 AND ?3")
    List<MealRecord> findByDepartmentAndDateRange(String department, LocalDateTime start, LocalDateTime end);
    
    @Query("SELECT COUNT(mr) FROM MealRecord mr WHERE mr.cardId = ?1 AND mr.mealType.id = ?2 AND DATE(mr.recordedAt) = ?3")
    long countByCardIdAndMealTypeIdAndDate(String cardId, String mealTypeId, LocalDate date);
    
    @Query("SELECT mr FROM MealRecord mr ORDER BY mr.recordedAt DESC")
    List<MealRecord> findAllOrderByRecordedAtDesc();
    
    @Query("SELECT mr FROM MealRecord mr WHERE mr.employee.employeeId = ?1 ORDER BY mr.recordedAt DESC")
    List<MealRecord> findByEmployeeIdOrderByRecordedAtDesc(String employeeId);
} 