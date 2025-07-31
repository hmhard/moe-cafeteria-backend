package et.moe.ethernet.cateteria.repository;

import et.moe.ethernet.cateteria.entity.MealType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MealTypeRepository extends JpaRepository<MealType, String> {
    
    List<MealType> findByIsActiveTrue();
    
    Optional<MealType> findByIdAndIsActiveTrue(String id);
    
    boolean existsByIdAndIsActiveTrue(String id);
} 