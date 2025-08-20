package et.moe.ethernet.cateteria.controller;

import et.moe.ethernet.cateteria.dto.CreateMealItemRequest;
import et.moe.ethernet.cateteria.dto.MealItemDto;
import et.moe.ethernet.cateteria.entity.MealItem;
import et.moe.ethernet.cateteria.service.MealItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/meal-items")
@RequiredArgsConstructor
@Tag(name = "Meal Items", description = "Operations for managing meal items (FirFir, Kinche, Salad, Pizza, Burger, etc.)")
public class MealItemController {
    
    private final MealItemService mealItemService;
    
    @GetMapping
    @Operation(
        summary = "Get all meal items",
        description = "Retrieve all meal items including disabled ones. Requires authentication."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved meal items"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Authentication required")
    })
    public ResponseEntity<List<MealItemDto>> getAllMealItems() {
        List<MealItemDto> items = mealItemService.getAllMealItems();
        return ResponseEntity.ok(items);
    }
    
    @GetMapping("/active")
    @Operation(
        summary = "Get active meal items",
        description = "Retrieve only active meal items. This endpoint is publicly accessible."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved active meal items")
    })
    public ResponseEntity<List<MealItemDto>> getActiveMealItems() {
        List<MealItemDto> items = mealItemService.getActiveMealItems();
        return ResponseEntity.ok(items);
    }
    
    @GetMapping("/by-category/{mealCategoryId}")
    @Operation(
        summary = "Get meal items by category",
        description = "Retrieve meal items for a specific meal category. This endpoint is publicly accessible."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved meal items for the category"),
        @ApiResponse(responseCode = "404", description = "Meal category not found")
    })
    public ResponseEntity<List<MealItemDto>> getMealItemsByCategory(@PathVariable String mealCategoryId) {
        List<MealItemDto> items = mealItemService.getMealItemsByCategory(mealCategoryId);
        return ResponseEntity.ok(items);
    }
    
    @GetMapping("/{id}")
    @Operation(
        summary = "Get meal item by ID",
        description = "Retrieve a specific meal item by its ID. Requires authentication."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved the meal item"),
        @ApiResponse(responseCode = "404", description = "Meal item not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Authentication required")
    })
    public ResponseEntity<MealItemDto> getMealItemById(@PathVariable String id) {
        return mealItemService.getMealItemById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    @Operation(
        summary = "Create a new meal item",
        description = "Create a new meal item with image upload, name, color, status, and total available. Requires manager role."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Meal item created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Authentication required"),
        @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions")
    })
    public ResponseEntity<MealItemDto> createMealItem(@RequestBody CreateMealItemRequest request) {
        try {
            MealItemDto createdItem = mealItemService.createMealItem(request);
            return ResponseEntity.status(201).body(createdItem);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}")
    @Operation(
        summary = "Update a meal item",
        description = "Update an existing meal item. Requires manager role."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Meal item updated successfully"),
        @ApiResponse(responseCode = "404", description = "Meal item not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Authentication required"),
        @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions")
    })
    public ResponseEntity<MealItemDto> updateMealItem(@PathVariable String id, @RequestBody MealItem mealItemUpdates) {
        return mealItemService.updateMealItem(id, mealItemUpdates)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @PatchMapping("/{id}/toggle")
    @Operation(
        summary = "Toggle meal item active status",
        description = "Toggle the active status of a meal item. Requires manager role."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Meal item status toggled successfully"),
        @ApiResponse(responseCode = "404", description = "Meal item not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Authentication required"),
        @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions")
    })
    public ResponseEntity<Boolean> toggleMealItemActive(@PathVariable String id) {
        boolean success = mealItemService.toggleMealItemActive(id);
        return success ? ResponseEntity.ok(true) : ResponseEntity.notFound().build();
    }
    
    @PatchMapping("/{id}/availability")
    @Operation(
        summary = "Update meal item availability",
        description = "Update the total available quantity for a meal item. Requires manager role."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Availability updated successfully"),
        @ApiResponse(responseCode = "404", description = "Meal item not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Authentication required"),
        @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions")
    })
    public ResponseEntity<Boolean> updateItemAvailability(@PathVariable String id, @RequestBody Integer totalAvailable) {
        boolean success = mealItemService.updateItemAvailability(id, totalAvailable);
        return success ? ResponseEntity.ok(true) : ResponseEntity.notFound().build();
    }
    
    @DeleteMapping("/{id}")
    @Operation(
        summary = "Delete a meal item",
        description = "Delete a meal item permanently. Requires manager role."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Meal item deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Meal item not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Authentication required"),
        @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions")
    })
    public ResponseEntity<Boolean> deleteMealItem(@PathVariable String id) {
        boolean success = mealItemService.deleteMealItem(id);
        return success ? ResponseEntity.ok(true) : ResponseEntity.notFound().build();
    }
} 