package et.moe.ethernet.cateteria.controller;

import et.moe.ethernet.cateteria.dto.EmployeeDto;
import et.moe.ethernet.cateteria.dto.EmployeeUsageStatsDto;
import et.moe.ethernet.cateteria.dto.MealRecordDto;
import et.moe.ethernet.cateteria.entity.Employee;
import et.moe.ethernet.cateteria.service.EmployeeService;
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
@RequestMapping("/employees")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
@Tag(name = "Employees", description = "Operations for managing employees and their support eligibility")
public class EmployeeController {
    
    private final EmployeeService employeeService;
    
    @GetMapping
    @Operation(
        summary = "Get all active employees",
        description = "Retrieve all active employees with their support eligibility status. Requires authentication."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved employees"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Authentication required")
    })
    public ResponseEntity<List<EmployeeDto>> getAllEmployees() {
        List<EmployeeDto> employees = employeeService.getAllEmployees();
        return ResponseEntity.ok(employees);
    }
    
    @GetMapping("/department/{department}")
    @Operation(
        summary = "Get employees by department",
        description = "Retrieve all active employees in a specific department. Requires authentication."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved employees"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Authentication required")
    })
    public ResponseEntity<List<EmployeeDto>> getEmployeesByDepartment(
        @Parameter(description = "Department name", example = "ኢንጂነሪንግ")
        @PathVariable String department
    ) {
        List<EmployeeDto> employees = employeeService.getEmployeesByDepartment(department);
        return ResponseEntity.ok(employees);
    }
    
    @GetMapping("/{id}")
    @Operation(
        summary = "Get employee by ID",
        description = "Retrieve a specific employee by their UUID. Requires authentication."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved employee"),
        @ApiResponse(responseCode = "404", description = "Employee not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Authentication required")
    })
    public ResponseEntity<EmployeeDto> getEmployeeById(
        @Parameter(description = "Employee UUID", example = "123e4567-e89b-12d3-a456-426614174000")
        @PathVariable String id
    ) {
        return employeeService.getEmployeeById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/by-employee-id/{employeeId}")
    @Operation(
        summary = "Get employee by employee ID",
        description = "Retrieve a specific employee by their employee ID. Requires authentication."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved employee"),
        @ApiResponse(responseCode = "404", description = "Employee not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Authentication required")
    })
    public ResponseEntity<EmployeeDto> getEmployeeByEmployeeId(
        @Parameter(description = "Employee ID", example = "EMP001")
        @PathVariable String employeeId
    ) {
        return employeeService.getEmployeeByEmployeeId(employeeId)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/by-card/{cardId}")
    @Operation(
        summary = "Get employee by card ID",
        description = "Retrieve an active employee by their card ID. This endpoint is publicly accessible for meal recording."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved employee"),
        @ApiResponse(responseCode = "404", description = "Employee not found or inactive")
    })
    public ResponseEntity<EmployeeDto> getEmployeeByCardId(
        @Parameter(description = "Card ID", example = "04A2B3C4D5")
        @PathVariable String cardId
    ) {
        return employeeService.getEmployeeByCardId(cardId)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/by-code/{shortCode}")
    @Operation(
        summary = "Get employee by short code",
        description = "Retrieve an active employee by their short code. This endpoint is publicly accessible for meal recording."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved employee"),
        @ApiResponse(responseCode = "404", description = "Employee not found or inactive")
    })
    public ResponseEntity<EmployeeDto> getEmployeeByShortCode(
        @Parameter(description = "Short code (4 digits)", example = "1234")
        @PathVariable String shortCode
    ) {
        return employeeService.getEmployeeByShortCode(shortCode)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    @Operation(
        summary = "Create a new employee",
        description = "Create a new employee. Requires authentication."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully created employee"),
        @ApiResponse(responseCode = "400", description = "Bad request - Validation error"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Authentication required")
    })
    public ResponseEntity<EmployeeDto> createEmployee(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Employee to create",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Employee.class),
                examples = @ExampleObject(
                    name = "Employee Example",
                    value = """
                        {
                          "employeeId": "EMP006",
                          "cardId": "59D0E1F2G3",
                          "shortCode": "1111",
                          "name": "የሚስተር አበበ",
                          "department": "ኢንጂነሪንግ",
                          "salary": 4800.00,
                          "photoUrl": "/placeholder.jpg",
                          "isActive": true
                        }
                        """
                )
            )
        )
        @RequestBody Employee employee
    ) {
        EmployeeDto createdEmployee = employeeService.createEmployee(employee);
        return ResponseEntity.ok(createdEmployee);
    }
    
    @PutMapping("/{id}")
    @Operation(
        summary = "Update an employee",
        description = "Update an existing employee by ID. Requires authentication."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully updated employee"),
        @ApiResponse(responseCode = "404", description = "Employee not found"),
        @ApiResponse(responseCode = "400", description = "Bad request - Validation error"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Authentication required")
    })
    public ResponseEntity<EmployeeDto> updateEmployee(
        @Parameter(description = "Employee UUID", example = "123e4567-e89b-12d3-a456-426614174000")
        @PathVariable String id,
        @RequestBody Employee employee
    ) {
        return employeeService.updateEmployee(id, employee)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @PatchMapping("/{id}/toggle")
    @Operation(
        summary = "Toggle employee active status",
        description = "Activate or deactivate an employee. Requires authentication."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully toggled employee status"),
        @ApiResponse(responseCode = "404", description = "Employee not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Authentication required")
    })
    public ResponseEntity<Void> toggleEmployeeStatus(
        @Parameter(description = "Employee UUID", example = "123e4567-e89b-12d3-a456-426614174000")
        @PathVariable String id
    ) {
        boolean success = employeeService.toggleEmployeeStatus(id);
        return success ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
    
    @DeleteMapping("/{id}")
    @Operation(
        summary = "Delete an employee",
        description = "Delete an employee by ID. Requires authentication."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully deleted employee"),
        @ApiResponse(responseCode = "404", description = "Employee not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Authentication required")
    })
    public ResponseEntity<Void> deleteEmployee(
        @Parameter(description = "Employee UUID", example = "123e4567-e89b-12d3-a456-426614174000")
        @PathVariable String id
    ) {
        boolean success = employeeService.deleteEmployee(id);
        return success ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
    
    @GetMapping("/{id}/usage-stats")
    @Operation(
        summary = "Get employee usage statistics",
        description = "Retrieve usage statistics for a specific employee including meal counts and amounts. Requires authentication."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved usage statistics"),
        @ApiResponse(responseCode = "404", description = "Employee not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Authentication required")
    })
    public ResponseEntity<EmployeeUsageStatsDto> getEmployeeUsageStats(
        @Parameter(description = "Employee ID", example = "EMP001")
        @PathVariable String id
    ) {
        return employeeService.getEmployeeUsageStats(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/{id}/meal-records")
    @Operation(
        summary = "Get employee meal records",
        description = "Retrieve all meal records for a specific employee. Requires authentication."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved meal records"),
        @ApiResponse(responseCode = "404", description = "Employee not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Authentication required")
    })
    public ResponseEntity<List<MealRecordDto>> getEmployeeMealRecords(
        @Parameter(description = "Employee ID", example = "EMP001")
        @PathVariable String id
    ) {
        return employeeService.getEmployeeMealRecords(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/support-eligible")
    @Operation(
        summary = "Get employees eligible for support",
        description = "Retrieve all employees who are eligible for supported pricing based on salary threshold. Requires authentication."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved eligible employees"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Authentication required")
    })
    public ResponseEntity<List<EmployeeDto>> getEligibleEmployeesForSupport() {
        List<EmployeeDto> employees = employeeService.getEligibleEmployeesForSupport();
        return ResponseEntity.ok(employees);
    }
} 