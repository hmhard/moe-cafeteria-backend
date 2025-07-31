package et.moe.ethernet.cateteria.controller;

import et.moe.ethernet.cateteria.dto.MealRecordDto;
import et.moe.ethernet.cateteria.service.MealRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/meal-records")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
@Tag(name = "Meal Records", description = "Operations for recording and retrieving meal transactions")
public class MealRecordController {
    
    private final MealRecordService mealRecordService;
    
    @GetMapping
    @Operation(
        summary = "Get all meal records",
        description = "Retrieve all meal records ordered by recorded date (newest first). Requires authentication."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved meal records"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Authentication required")
    })
    public ResponseEntity<List<MealRecordDto>> getAllMealRecords() {
        List<MealRecordDto> records = mealRecordService.getAllMealRecords();
        return ResponseEntity.ok(records);
    }
    
    @GetMapping("/employee/{employeeId}")
    @Operation(
        summary = "Get meal records by employee",
        description = "Retrieve all meal records for a specific employee. Requires authentication."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved meal records"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Authentication required")
    })
    public ResponseEntity<List<MealRecordDto>> getMealRecordsByEmployee(
        @Parameter(description = "Employee ID", example = "EMP001")
        @PathVariable String employeeId
    ) {
        List<MealRecordDto> records = mealRecordService.getMealRecordsByEmployee(employeeId);
        return ResponseEntity.ok(records);
    }
    
    @GetMapping("/date-range")
    @Operation(
        summary = "Get meal records by date range",
        description = "Retrieve meal records within a specific date range. Requires authentication."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved meal records"),
        @ApiResponse(responseCode = "400", description = "Bad request - Invalid date format"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Authentication required")
    })
    public ResponseEntity<List<MealRecordDto>> getMealRecordsByDateRange(
        @Parameter(description = "Start date and time", example = "2024-01-01T00:00:00")
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
        @Parameter(description = "End date and time", example = "2024-01-31T23:59:59")
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end
    ) {
        List<MealRecordDto> records = mealRecordService.getMealRecordsByDateRange(start, end);
        return ResponseEntity.ok(records);
    }
    
    @GetMapping("/department/{department}/date-range")
    @Operation(
        summary = "Get meal records by department and date range",
        description = "Retrieve meal records for a specific department within a date range. Requires authentication."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved meal records"),
        @ApiResponse(responseCode = "400", description = "Bad request - Invalid date format"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Authentication required")
    })
    public ResponseEntity<List<MealRecordDto>> getMealRecordsByDepartmentAndDateRange(
        @Parameter(description = "Department name", example = "ኢንጂነሪንግ")
        @PathVariable String department,
        @Parameter(description = "Start date and time", example = "2024-01-01T00:00:00")
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
        @Parameter(description = "End date and time", example = "2024-01-31T23:59:59")
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end
    ) {
        List<MealRecordDto> records = mealRecordService.getMealRecordsByDepartmentAndDateRange(department, start, end);
        return ResponseEntity.ok(records);
    }
    
    @GetMapping("/{id}")
    @Operation(
        summary = "Get meal record by ID",
        description = "Retrieve a specific meal record by its UUID. Requires authentication."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved meal record"),
        @ApiResponse(responseCode = "404", description = "Meal record not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Authentication required")
    })
    public ResponseEntity<MealRecordDto> getMealRecordById(
        @Parameter(description = "Meal record UUID", example = "123e4567-e89b-12d3-a456-426614174000")
        @PathVariable String id
    ) {
        return mealRecordService.getMealRecordById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/check-duplicate")
    @Operation(
        summary = "Check for duplicate meal records",
        description = "Check if an employee has already used a specific meal type today. This endpoint is publicly accessible."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully checked for duplicates"),
        @ApiResponse(responseCode = "400", description = "Bad request - Invalid parameters"),
        @ApiResponse(responseCode = "404", description = "Employee not found")
    })
    public ResponseEntity<Object> checkDuplicate(
        @Parameter(description = "Employee card ID", example = "04A2B3C4D5")
        @RequestParam String cardId,
        @Parameter(description = "Meal type ID", example = "breakfast")
        @RequestParam String mealTypeId
    ) {
        boolean hasUsed = mealRecordService.hasUsedMealTypeToday(cardId, mealTypeId);
        return ResponseEntity.ok(Map.of(
            "hasUsedToday", hasUsed,
            "cardId", cardId,
            "mealTypeId", mealTypeId,
            "date", LocalDate.now().toString()
        ));
    }
    
    @PostMapping("/record")
    @Operation(
        summary = "Record a meal transaction",
        description = "Record a meal transaction for an employee. This endpoint is publicly accessible for meal recording."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully recorded meal", content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = MealRecordDto.class),
            examples = @ExampleObject(
                name = "Meal Record Response",
                value = """
                    {
                      "id": "123e4567-e89b-12d3-a456-426614174000",
                      "employeeId": "EMP001",
                      "cardId": "04A2B3C4D5",
                      "mealTypeId": "breakfast",
                      "mealCategoryId": "456e7890-e89b-12d3-a456-426614174000",
                      "mealName": "ቁርስ - ጾም",
                      "category": "fasting",
                      "priceType": "supported",
                      "normalPrice": 30.00,
                      "supportedPrice": 20.00,
                      "actualPrice": 20.00,
                      "supportAmount": 10.00,
                      "employeeSalary": 4500.00,
                      "timestamp": "2024-01-15T08:30:00",
                      "createdAt": "2024-01-15T08:30:00"
                    }
                    """
            )
        )),
        @ApiResponse(responseCode = "400", description = "Bad request - Employee not found or meal already recorded today"),
        @ApiResponse(responseCode = "404", description = "Employee or meal category not found")
    })
    public ResponseEntity<Object> recordMeal(
        @Parameter(description = "Meal record request")
        @RequestBody Map<String, String> request
    ) {
        String cardId = request.get("cardId");
        String mealCategoryId = request.get("mealCategoryId");
        
        if (cardId == null || mealCategoryId == null) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", "Missing required parameters: cardId and mealCategoryId"
            ));
        }
        
        try {
            MealRecordDto record = mealRecordService.recordMeal(cardId, mealCategoryId);
            return ResponseEntity.ok(record);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", e.getMessage()
            ));
        }
    }
} 