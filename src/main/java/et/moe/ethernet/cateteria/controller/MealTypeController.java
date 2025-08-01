package et.moe.ethernet.cateteria.controller;

import et.moe.ethernet.cateteria.dto.MealTypeDto;
import et.moe.ethernet.cateteria.entity.MealType;
import et.moe.ethernet.cateteria.service.MealTypeService;
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
@RequestMapping("/meal-types")
@RequiredArgsConstructor
@Tag(name = "Meal Types", description = "Operations for managing meal types (breakfast, lunch, etc.)")
public class MealTypeController {
    
    private final MealTypeService mealTypeService;
    
    @GetMapping
    @Operation(
        summary = "Get all meal types",
        description = "Retrieve all meal types including disabled ones. Requires authentication."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved meal types"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Authentication required")
    })
    public ResponseEntity<List<MealTypeDto>> getAllMealTypes() {
        List<MealTypeDto> mealTypes = mealTypeService.getAllMealTypes();
        return ResponseEntity.ok(mealTypes);
    }
    
    @GetMapping("/active")
    @Operation(
        summary = "Get active meal types",
        description = "Retrieve only active meal types. This endpoint is publicly accessible."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved active meal types")
    })
    public ResponseEntity<List<MealTypeDto>> getActiveMealTypes() {
        List<MealTypeDto> mealTypes = mealTypeService.getActiveMealTypes();
        return ResponseEntity.ok(mealTypes);
    }
    
    @GetMapping("/{id}")
    @Operation(
        summary = "Get meal type by ID",
        description = "Retrieve a specific meal type by its ID. Requires authentication."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved meal type"),
        @ApiResponse(responseCode = "404", description = "Meal type not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Authentication required")
    })
    public ResponseEntity<MealTypeDto> getMealTypeById(
        @Parameter(description = "ID of the meal type", example = "breakfast")
        @PathVariable String id
    ) {
        return mealTypeService.getMealTypeById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    @Operation(
        summary = "Create a new meal type",
        description = "Create a new meal type. Requires authentication."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully created meal type"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Authentication required")
    })
    public ResponseEntity<MealTypeDto> createMealType(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Meal type to create",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = MealType.class),
                examples = @ExampleObject(
                    name = "Breakfast Example",
                    value = """
                        {
                          "id": "breakfast",
                          "name": "ቁርስ",
                          "icon": "COFFEE",
                          "isActive": true,
                          "color": "bg-amber-100 text-amber-700"
                        }
                        """
                )
            )
        )
        @RequestBody MealType mealType
    ) {
        MealTypeDto createdMealType = mealTypeService.createMealType(mealType);
        return ResponseEntity.ok(createdMealType);
    }
    
    @PutMapping("/{id}")
    @Operation(
        summary = "Update a meal type",
        description = "Update an existing meal type by ID. Requires authentication."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully updated meal type"),
        @ApiResponse(responseCode = "404", description = "Meal type not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Authentication required")
    })
    public ResponseEntity<MealTypeDto> updateMealType(
        @Parameter(description = "ID of the meal type to update", example = "breakfast")
        @PathVariable String id,
        @RequestBody MealType mealType
    ) {
        return mealTypeService.updateMealType(id, mealType)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @PatchMapping("/{id}/toggle")
    @Operation(
        summary = "Toggle meal type active status",
        description = "Activate or deactivate a meal type. Requires authentication."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully toggled meal type status"),
        @ApiResponse(responseCode = "404", description = "Meal type not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Authentication required")
    })
    public ResponseEntity<Void> toggleMealTypeActive(
        @Parameter(description = "ID of the meal type to toggle", example = "breakfast")
        @PathVariable String id
    ) {
        boolean success = mealTypeService.toggleMealTypeActive(id);
        return success ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
    
    @DeleteMapping("/{id}")
    @Operation(
        summary = "Delete a meal type",
        description = "Delete a meal type by ID. Requires authentication."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully deleted meal type"),
        @ApiResponse(responseCode = "404", description = "Meal type not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Authentication required")
    })
    public ResponseEntity<Void> deleteMealType(
        @Parameter(description = "ID of the meal type to delete", example = "breakfast")
        @PathVariable String id
    ) {
        boolean success = mealTypeService.deleteMealType(id);
        return success ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
} 