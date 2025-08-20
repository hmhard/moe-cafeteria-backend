package et.moe.ethernet.cateteria.repository;

import et.moe.ethernet.cateteria.entity.MealItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MealItemRepository extends JpaRepository<MealItem, String> {
    
    List<MealItem> findByIsActiveTrue();
    
    List<MealItem> findByMealCategoryIdAndIsActiveTrue(String mealCategoryId);
    
    List<MealItem> findByMealCategoryId(String mealCategoryId);
    
    Optional<MealItem> findByIdAndIsActiveTrue(String id);
    
    boolean existsByNameAndMealCategoryId(String name, String mealCategoryId);
} 