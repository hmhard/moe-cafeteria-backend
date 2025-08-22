package et.moe.ethernet.cateteria.controller;

import et.moe.ethernet.cateteria.service.SupportReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/support-reports")
@RequiredArgsConstructor
@Tag(name = "Support Reports", description = "Support report endpoints")
public class SupportReportController {
    
    private final SupportReportService supportReportService;
    
    @GetMapping("/summary")
    @Operation(summary = "Get support summary", description = "Get support summary statistics for a given period")
    public ResponseEntity<SupportReportService.SupportSummary> getSupportSummary(
            @RequestParam(defaultValue = "monthly") String period) {
        SupportReportService.SupportSummary summary = supportReportService.getSupportSummary(period);
        return ResponseEntity.ok(summary);
    }
    
    @GetMapping("/department-analysis")
    @Operation(summary = "Get department analysis", description = "Get department-wise support analysis for a given period")
    public ResponseEntity<List<SupportReportService.DepartmentSupportAnalysis>> getDepartmentAnalysis(
            @RequestParam(defaultValue = "monthly") String period) {
        List<SupportReportService.DepartmentSupportAnalysis> analysis = supportReportService.getDepartmentAnalysis(period);
        return ResponseEntity.ok(analysis);
    }
    
    @GetMapping("/department-analysis/paginated")
    @Operation(summary = "Get paginated department analysis", description = "Get paginated department-wise support analysis for a given period")
    public ResponseEntity<SupportReportService.PaginatedDepartmentAnalysis> getPaginatedDepartmentAnalysis(
            @RequestParam(defaultValue = "monthly") String period,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        SupportReportService.PaginatedDepartmentAnalysis analysis = supportReportService.getPaginatedDepartmentAnalysis(period, page, size);
        return ResponseEntity.ok(analysis);
    }
    
    @GetMapping("/category-usage")
    @Operation(summary = "Get meal category usage", description = "Get usage per meal category for a given period")
    public ResponseEntity<List<SupportReportService.MealCategoryUsage>> getMealCategoryUsage(
            @RequestParam(defaultValue = "monthly") String period) {
        return ResponseEntity.ok(supportReportService.getMealCategoryUsage(period));
    }
} 