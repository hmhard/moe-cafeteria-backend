package et.moe.ethernet.cateteria.repository;

import et.moe.ethernet.cateteria.entity.MealRecordItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MealRecordItemRepository extends JpaRepository<MealRecordItem, String> {
    
    /**
     * Find all meal record items for a specific meal record
     */
    List<MealRecordItem> findByMealRecordIdOrderByCreatedAtAsc(String mealRecordId);
    
    /**
     * Find all meal record items for multiple meal records
     */
    @Query("SELECT mri FROM MealRecordItem mri WHERE mri.mealRecord.id IN :mealRecordIds ORDER BY mri.mealRecord.id, mri.createdAt")
    List<MealRecordItem> findByMealRecordIds(@Param("mealRecordIds") List<String> mealRecordIds);
    
    /**
     * Delete all meal record items for a specific meal record
     */
    void deleteByMealRecordId(String mealRecordId);
} 