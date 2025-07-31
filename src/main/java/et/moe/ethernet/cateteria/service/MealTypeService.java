package et.moe.ethernet.cateteria.service;

import et.moe.ethernet.cateteria.dto.MealTypeDto;
import et.moe.ethernet.cateteria.entity.MealType;
import et.moe.ethernet.cateteria.repository.MealTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MealTypeService {
    
    private final MealTypeRepository mealTypeRepository;
    
    public List<MealTypeDto> getAllMealTypes() {
        return mealTypeRepository.findAll().stream()
            .map(MealTypeDto::fromEntity)
            .collect(Collectors.toList());
    }
    
    public List<MealTypeDto> getActiveMealTypes() {
        return mealTypeRepository.findByIsActiveTrue().stream()
            .map(MealTypeDto::fromEntity)
            .collect(Collectors.toList());
    }
    
    public Optional<MealTypeDto> getMealTypeById(String id) {
        return mealTypeRepository.findById(id)
            .map(MealTypeDto::fromEntity);
    }
    
    public Optional<MealTypeDto> getActiveMealTypeById(String id) {
        return mealTypeRepository.findByIdAndIsActiveTrue(id)
            .map(MealTypeDto::fromEntity);
    }
    
    public MealTypeDto createMealType(MealType mealType) {
        MealType savedMealType = mealTypeRepository.save(mealType);
        return MealTypeDto.fromEntity(savedMealType);
    }
    
    public Optional<MealTypeDto> updateMealType(String id, MealType mealTypeUpdates) {
        return mealTypeRepository.findById(id)
            .map(existingMealType -> {
                existingMealType.setName(mealTypeUpdates.getName());
                existingMealType.setIcon(mealTypeUpdates.getIcon());
                existingMealType.setColor(mealTypeUpdates.getColor());
                existingMealType.setActive(mealTypeUpdates.isActive());
                return MealTypeDto.fromEntity(mealTypeRepository.save(existingMealType));
            });
    }
    
    public boolean toggleMealTypeActive(String id) {
        return mealTypeRepository.findById(id)
            .map(mealType -> {
                mealType.setActive(!mealType.isActive());
                mealTypeRepository.save(mealType);
                return true;
            })
            .orElse(false);
    }
    
    public boolean deleteMealType(String id) {
        if (mealTypeRepository.existsById(id)) {
            mealTypeRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    public boolean existsById(String id) {
        return mealTypeRepository.existsById(id);
    }
    
    public boolean existsByIdAndActive(String id) {
        return mealTypeRepository.existsByIdAndIsActiveTrue(id);
    }
} 