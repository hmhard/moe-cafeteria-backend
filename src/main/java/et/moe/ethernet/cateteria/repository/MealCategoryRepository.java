package et.moe.ethernet.cateteria.repository;

import et.moe.ethernet.cateteria.entity.MealCategory;
import et.moe.ethernet.cateteria.entity.MealType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MealCategoryRepository extends JpaRepository<MealCategory, String> {
    
    List<MealCategory> findByIsActiveTrue();
    
    List<MealCategory> findByMealTypeAndIsActiveTrue(MealType mealType);
    
    List<MealCategory> findByMealTypeIdAndIsActiveTrue(String mealTypeId);
    
    Optional<MealCategory> findByIdAndIsActiveTrue(String id);
    
    boolean existsByMealTypeAndCategory(MealType mealType, MealCategory.MealCategoryType category);
} 