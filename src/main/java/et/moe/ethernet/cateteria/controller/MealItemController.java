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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/meal-items")
@RequiredArgsConstructor
@Tag(name = "Meal Items", description = "Operations for managing meal items (FirFir, Kinche, Salad, Pizza, Burger, etc.)")
public class MealItemController {
    
    private final MealItemService mealItemService;
    private static final Path UPLOAD_DIR = Paths.get("uploads");
    
    private String saveFile(MultipartFile file) throws IOException {
        if (!Files.exists(UPLOAD_DIR)) {
            Files.createDirectories(UPLOAD_DIR);
        }
        String original = file.getOriginalFilename() != null ? file.getOriginalFilename() : "file";
        String filename = Instant.now().toEpochMilli() + "-" + original.replaceAll("[^a-zA-Z0-9.-]", "_");
        Path target = UPLOAD_DIR.resolve(filename);
        Files.copy(file.getInputStream(), target);
        return "/uploads/" + filename;
    }
    
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
    
    @PostMapping(consumes = {"multipart/form-data"})
    @Operation(
        summary = "Create a new meal item",
        description = "Create a new meal item with optional image upload."
    )
    public ResponseEntity<MealItemDto> createMealItemMultipart(
        @RequestPart("name") String name,
        @RequestPart(value = "description", required = false) String description,
        @RequestPart("mealCategoryId") String mealCategoryId,
        @RequestPart(value = "color", required = false) String color,
        @RequestPart(value = "totalAvailable", required = false) Integer totalAvailable,
        @RequestPart(value = "isActive", required = false) Boolean isActive,
        @RequestPart(value = "file", required = false) MultipartFile file
    ) throws IOException {
        CreateMealItemRequest req = new CreateMealItemRequest();
        req.setName(name);
        req.setDescription(description);
        req.setMealCategoryId(mealCategoryId);
        req.setColor(color);
        req.setTotalAvailable(totalAvailable);
        req.setActive(isActive != null ? isActive : true);
        if (file != null && !file.isEmpty()) {
            String url = saveFile(file);
            req.setImageUrl(url);
        }
        MealItemDto created = mealItemService.createMealItem(req);
        return ResponseEntity.status(201).body(created);
    }
    
    @PutMapping(value = "/{id}")
    @Operation(summary = "Update a meal item", description = "Update a meal item, allowing optional image upload.")
    public ResponseEntity<MealItemDto> updateMealItem(
        @PathVariable String id,
        @RequestParam(value = "name", required = false) String name,
        @RequestParam(value = "description", required = false) String description,
        @RequestParam(value = "mealCategoryId", required = false) String mealCategoryId,
        @RequestParam(value = "color", required = false) String color,
        @RequestParam(value = "totalAvailable", required = false) Integer totalAvailable,
        @RequestParam(value = "isActive", required = false) Boolean isActive,
        @RequestParam(value = "file", required = false) MultipartFile file
    ) throws IOException {
        MealItem updates = new MealItem();
        updates.setName(name);
        updates.setDescription(description);
        updates.setColor(color);
        updates.setTotalAvailable(totalAvailable);
        if (isActive != null) updates.setActive(isActive);
        if (file != null && !file.isEmpty()) {
            String url = saveFile(file);
            updates.setImageUrl(url);
        }
        return mealItemService.updateMealItem(id, updates)
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