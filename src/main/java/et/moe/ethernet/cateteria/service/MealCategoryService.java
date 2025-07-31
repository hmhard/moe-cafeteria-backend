package et.moe.ethernet.cateteria.service;

import et.moe.ethernet.cateteria.dto.CreateMealCategoryRequest;
import et.moe.ethernet.cateteria.dto.MealCategoryDto;
import et.moe.ethernet.cateteria.entity.MealCategory;
import et.moe.ethernet.cateteria.entity.MealType;
import et.moe.ethernet.cateteria.repository.MealCategoryRepository;
import et.moe.ethernet.cateteria.repository.MealTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MealCategoryService {
    
    private final MealCategoryRepository mealCategoryRepository;
    private final MealTypeRepository mealTypeRepository;
    
    public List<MealCategoryDto> getAllMealCategories() {
        return mealCategoryRepository.findAll().stream()
            .map(MealCategoryDto::fromEntity)
            .collect(Collectors.toList());
    }
    
    public List<MealCategoryDto> getActiveMealCategories() {
        return mealCategoryRepository.findByIsActiveTrue().stream()
            .map(MealCategoryDto::fromEntity)
            .collect(Collectors.toList());
    }
    
    public List<MealCategoryDto> getMealCategoriesByType(String mealTypeId) {
        return mealCategoryRepository.findByMealTypeIdAndIsActiveTrue(mealTypeId).stream()
            .map(MealCategoryDto::fromEntity)
            .collect(Collectors.toList());
    }
    
    public Optional<MealCategoryDto> getMealCategoryById(String id) {
        return mealCategoryRepository.findById(id)
            .map(MealCategoryDto::fromEntity);
    }
    
    public Optional<MealCategoryDto> getActiveMealCategoryById(String id) {
        return mealCategoryRepository.findByIdAndIsActiveTrue(id)
            .map(MealCategoryDto::fromEntity);
    }
    
    public MealCategoryDto createMealCategory(CreateMealCategoryRequest request) {
        // Validate meal type exists
        MealType mealType = mealTypeRepository.findById(request.getMealTypeId())
            .orElseThrow(() -> new RuntimeException("Meal type not found with ID: " + request.getMealTypeId()));
        
        MealCategory mealCategory = new MealCategory();
        mealCategory.setName(request.getName());
        mealCategory.setMealType(mealType);
        mealCategory.setCategory(MealCategory.MealCategoryType.valueOf(request.getCategory().toUpperCase()));
        mealCategory.setNormalPrice(request.getNormalPrice());
        mealCategory.setSupportedPrice(request.getSupportedPrice());
        mealCategory.setActive(request.isActive());
        
        MealCategory savedCategory = mealCategoryRepository.save(mealCategory);
        return MealCategoryDto.fromEntity(savedCategory);
    }
    
    public Optional<MealCategoryDto> updateMealCategory(String id, MealCategory mealCategoryUpdates) {
        return mealCategoryRepository.findById(id)
            .map(existingCategory -> {
                existingCategory.setName(mealCategoryUpdates.getName());
                existingCategory.setNormalPrice(mealCategoryUpdates.getNormalPrice());
                existingCategory.setSupportedPrice(mealCategoryUpdates.getSupportedPrice());
                existingCategory.setActive(mealCategoryUpdates.isActive());
                return MealCategoryDto.fromEntity(mealCategoryRepository.save(existingCategory));
            });
    }
    
    public boolean toggleMealCategoryActive(String id) {
        return mealCategoryRepository.findById(id)
            .map(category -> {
                category.setActive(!category.isActive());
                mealCategoryRepository.save(category);
                return true;
            })
            .orElse(false);
    }
    
    public boolean deleteMealCategory(String id) {
        if (mealCategoryRepository.existsById(id)) {
            mealCategoryRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    public boolean existsById(String id) {
        return mealCategoryRepository.existsById(id);
    }
    
    public boolean existsByIdAndActive(String id) {
        return mealCategoryRepository.findByIdAndIsActiveTrue(id).isPresent();
    }
} 