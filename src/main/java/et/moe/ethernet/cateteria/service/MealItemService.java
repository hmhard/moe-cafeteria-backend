package et.moe.ethernet.cateteria.service;

import et.moe.ethernet.cateteria.dto.CreateMealItemRequest;
import et.moe.ethernet.cateteria.dto.MealItemDto;
import et.moe.ethernet.cateteria.entity.MealCategory;
import et.moe.ethernet.cateteria.entity.MealItem;
import et.moe.ethernet.cateteria.repository.MealCategoryRepository;
import et.moe.ethernet.cateteria.repository.MealItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MealItemService {
    
    private final MealItemRepository mealItemRepository;
    private final MealCategoryRepository mealCategoryRepository;
    
    public List<MealItemDto> getAllMealItems() {
        return mealItemRepository.findAll().stream()
            .map(MealItemDto::fromEntity)
            .collect(Collectors.toList());
    }
    
    public List<MealItemDto> getActiveMealItems() {
        return mealItemRepository.findByIsActiveTrue().stream()
            .map(MealItemDto::fromEntity)
            .collect(Collectors.toList());
    }
    
    public List<MealItemDto> getMealItemsByCategory(String mealCategoryId) {
        return mealItemRepository.findByMealCategoryIdAndIsActiveTrue(mealCategoryId).stream()
            .map(MealItemDto::fromEntity)
            .collect(Collectors.toList());
    }
    
    public Optional<MealItemDto> getMealItemById(String id) {
        return mealItemRepository.findById(id)
            .map(MealItemDto::fromEntity);
    }
    
    public Optional<MealItemDto> getActiveMealItemById(String id) {
        return mealItemRepository.findByIdAndIsActiveTrue(id)
            .map(MealItemDto::fromEntity);
    }
    
    public MealItemDto createMealItem(CreateMealItemRequest request) {
        // Validate meal category exists
        MealCategory mealCategory = mealCategoryRepository.findById(request.getMealCategoryId())
            .orElseThrow(() -> new RuntimeException("Meal category not found with ID: " + request.getMealCategoryId()));
        
        // Check if item with same name already exists in this category
        if (mealItemRepository.existsByNameAndMealCategoryId(request.getName(), request.getMealCategoryId())) {
            throw new RuntimeException("Meal item with name '" + request.getName() + "' already exists in this category");
        }
        
        MealItem mealItem = new MealItem();
        mealItem.setName(request.getName());
        mealItem.setDescription(request.getDescription());
        mealItem.setMealCategory(mealCategory);
        mealItem.setImageUrl(request.getImageUrl());
        mealItem.setColor(request.getColor() != null ? request.getColor() : "#3B82F6");
        mealItem.setTotalAvailable(request.getTotalAvailable() != null ? request.getTotalAvailable() : 0);
        mealItem.setActive(request.isActive());
        
        MealItem savedItem = mealItemRepository.save(mealItem);
        return MealItemDto.fromEntity(savedItem);
    }
    
    public Optional<MealItemDto> updateMealItem(String id, MealItem mealItemUpdates) {
        return mealItemRepository.findById(id)
            .map(existingItem -> {
                if (mealItemUpdates.getName() != null) {
                    existingItem.setName(mealItemUpdates.getName());
                }
                if (mealItemUpdates.getDescription() != null) {
                    existingItem.setDescription(mealItemUpdates.getDescription());
                }
                if (mealItemUpdates.getImageUrl() != null) {
                    existingItem.setImageUrl(mealItemUpdates.getImageUrl());
                }
                if (mealItemUpdates.getColor() != null) {
                    existingItem.setColor(mealItemUpdates.getColor());
                }
                if (mealItemUpdates.getTotalAvailable() != null) {
                    existingItem.setTotalAvailable(mealItemUpdates.getTotalAvailable());
                }
                existingItem.setActive(mealItemUpdates.isActive());
                
                return MealItemDto.fromEntity(mealItemRepository.save(existingItem));
            });
    }
    
    public boolean toggleMealItemActive(String id) {
        return mealItemRepository.findById(id)
            .map(item -> {
                item.setActive(!item.isActive());
                mealItemRepository.save(item);
                return true;
            })
            .orElse(false);
    }
    
    public boolean deleteMealItem(String id) {
        if (mealItemRepository.existsById(id)) {
            mealItemRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    public boolean updateItemAvailability(String id, Integer totalAvailable) {
        return mealItemRepository.findById(id)
            .map(item -> {
                item.setTotalAvailable(totalAvailable);
                mealItemRepository.save(item);
                return true;
            })
            .orElse(false);
    }
} 