package et.moe.ethernet.cateteria.controller;

import et.moe.ethernet.cateteria.dto.CreateMealCategoryRequest;
import et.moe.ethernet.cateteria.dto.MealCategoryDto;
import et.moe.ethernet.cateteria.entity.MealCategory;
import et.moe.ethernet.cateteria.service.MealCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/meal-categories")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
@Tag(name = "Meal Categories", description = "Operations for managing meal categories (fasting/non-fasting)")
public class MealCategoryController {
    
    private final MealCategoryService mealCategoryService;
    
    @GetMapping
    @Operation(
        summary = "Get all meal categories",
        description = "Retrieve all meal categories including disabled ones. Requires authentication."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved meal categories"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Authentication required")
    })
    public ResponseEntity<List<MealCategoryDto>> getAllMealCategories() {
        List<MealCategoryDto> categories = mealCategoryService.getAllMealCategories();
        return ResponseEntity.ok(categories);
    }
    
    @GetMapping("/active")
    @Operation(
        summary = "Get active meal categories",
        description = "Retrieve only active meal categories. This endpoint is publicly accessible."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved active meal categories")
    })
    public ResponseEntity<List<MealCategoryDto>> getActiveMealCategories() {
        List<MealCategoryDto> categories = mealCategoryService.getActiveMealCategories();
        return ResponseEntity.ok(categories);
    }
    
    @GetMapping("/by-type/{mealTypeId}")
    @Operation(
        summary = "Get meal categories by meal type",
        description = "Retrieve active meal categories for a specific meal type. This endpoint is publicly accessible."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved meal categories for meal type"),
        @ApiResponse(responseCode = "404", description = "Meal type not found")
    })
    public ResponseEntity<List<MealCategoryDto>> getMealCategoriesByType(
        @Parameter(description = "ID of the meal type", example = "breakfast")
        @PathVariable String mealTypeId
    ) {
        List<MealCategoryDto> categories = mealCategoryService.getMealCategoriesByType(mealTypeId);
        return ResponseEntity.ok(categories);
    }
    
    @GetMapping("/{id}")
    @Operation(
        summary = "Get meal category by ID",
        description = "Retrieve a specific meal category by its ID. Requires authentication."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved meal category"),
        @ApiResponse(responseCode = "404", description = "Meal category not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Authentication required")
    })
    public ResponseEntity<MealCategoryDto> getMealCategoryById(
        @Parameter(description = "ID of the meal category")
        @PathVariable String id
    ) {
        return mealCategoryService.getMealCategoryById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    @Operation(
        summary = "Create a new meal category",
        description = "Create a new meal category. Requires authentication."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully created meal category"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Authentication required")
    })
    public ResponseEntity<MealCategoryDto> createMealCategory(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Meal category to create",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = CreateMealCategoryRequest.class),
                examples = @ExampleObject(
                    name = "Fasting Breakfast Example",
                    value = """
                        {
                          "name": "ቁርስ - ጾም",
                          "mealTypeId": "breakfast",
                          "category": "fasting",
                          "normalPrice": 30.00,
                          "supportedPrice": 20.00,
                          "isActive": true
                        }
                        """
                )
            )
        )
        @RequestBody CreateMealCategoryRequest request
    ) {
        MealCategoryDto createdCategory = mealCategoryService.createMealCategory(request);
        return ResponseEntity.ok(createdCategory);
    }
    
    @PutMapping("/{id}")
    @Operation(
        summary = "Update a meal category",
        description = "Update an existing meal category by ID. Requires authentication."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully updated meal category"),
        @ApiResponse(responseCode = "404", description = "Meal category not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Authentication required")
    })
    public ResponseEntity<MealCategoryDto> updateMealCategory(
        @Parameter(description = "ID of the meal category to update")
        @PathVariable String id,
        @RequestBody MealCategory mealCategory
    ) {
        return mealCategoryService.updateMealCategory(id, mealCategory)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @PatchMapping("/{id}/toggle")
    @Operation(
        summary = "Toggle meal category active status",
        description = "Activate or deactivate a meal category. Requires authentication."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully toggled meal category status"),
        @ApiResponse(responseCode = "404", description = "Meal category not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Authentication required")
    })
    public ResponseEntity<Void> toggleMealCategoryActive(
        @Parameter(description = "ID of the meal category to toggle")
        @PathVariable String id
    ) {
        boolean success = mealCategoryService.toggleMealCategoryActive(id);
        return success ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
    
    @DeleteMapping("/{id}")
    @Operation(
        summary = "Delete a meal category",
        description = "Delete a meal category by ID. Requires authentication."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully deleted meal category"),
        @ApiResponse(responseCode = "404", description = "Meal category not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Authentication required")
    })
    public ResponseEntity<Void> deleteMealCategory(
        @Parameter(description = "ID of the meal category to delete")
        @PathVariable String id
    ) {
        boolean success = mealCategoryService.deleteMealCategory(id);
        return success ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
} 