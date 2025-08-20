package et.moe.ethernet.cateteria.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/print")
@RequiredArgsConstructor
@Tag(name = "Print", description = "Direct printing operations")
public class PrintController {
    
    @PostMapping("/test")
    @Operation(
        summary = "Test print using the working command format",
        description = "Test print using the exact command format that works"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully printed test page"),
        @ApiResponse(responseCode = "500", description = "Internal server error - Print command failed")
    })
    public ResponseEntity<Object> testPrint(
        @Parameter(description = "Printer name", example = "POS-80")
        @RequestParam(defaultValue = "POS-80") String printer
    ) {
        try {
            // Use the exact working command format
            String command = String.format("echo -e \"\\x1B\\x40Hello POS-80\\n This is the test page tttttttttttttttttttttttttttttttttttttttttttttttttttt\\x1D\\x56\\x00\" | lp -d %s", printer);
            
            Process process = Runtime.getRuntime().exec(new String[]{"bash", "-c", command});
            int exitCode = process.waitFor();
            
            if (exitCode == 0) {
                return ResponseEntity.ok(Map.of(
                    "message", "Test print successful",
                    "printer", printer,
                    "command", command
                ));
            } else {
                return ResponseEntity.internalServerError().body(Map.of(
                    "error", "Test print failed with exit code: " + exitCode,
                    "command", command
                ));
            }
            
        } catch (IOException | InterruptedException e) {
            return ResponseEntity.internalServerError().body(Map.of(
                "error", "Failed to execute test print command: " + e.getMessage()
            ));
        }
    }
    
    @PostMapping("/echo")
    @Operation(
        summary = "Print receipt using echo command",
        description = "Print receipt data using echo command with lp"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully printed"),
        @ApiResponse(responseCode = "400", description = "Bad request - Invalid print data"),
        @ApiResponse(responseCode = "500", description = "Internal server error - Print command failed")
    })
    public ResponseEntity<Object> printReceiptEcho(
        @Parameter(description = "Print request containing data and printer name")
        @RequestBody Map<String, String> request
    ) {
        String data = request.get("data");
        String printer = request.get("printer");
        
        if (data == null || printer == null) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", "Missing required parameters: data and printer"
            ));
        }
        
        try {
            // Create the echo command similar to the working example
            String command = String.format("echo -e \"%s\" | lp -d %s", 
                data.replace("\"", "\\\""), printer);
            
            Process process = Runtime.getRuntime().exec(new String[]{"bash", "-c", command});
            int exitCode = process.waitFor();
            
            if (exitCode == 0) {
                return ResponseEntity.ok(Map.of(
                    "message", "Receipt printed successfully via echo",
                    "printer", printer,
                    "command", command
                ));
            } else {
                return ResponseEntity.internalServerError().body(Map.of(
                    "error", "Echo print command failed with exit code: " + exitCode,
                    "command", command
                ));
            }
            
        } catch (IOException | InterruptedException e) {
            return ResponseEntity.internalServerError().body(Map.of(
                "error", "Failed to execute echo print command: " + e.getMessage()
            ));
        }
    }
    
    @PostMapping
    @Operation(
        summary = "Print receipt using lp command",
        description = "Print receipt data directly using the system lp command"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully printed"),
        @ApiResponse(responseCode = "400", description = "Bad request - Invalid print data"),
        @ApiResponse(responseCode = "500", description = "Internal server error - Print command failed")
    })
    public ResponseEntity<Object> printReceipt(
        @Parameter(description = "Print request containing data and printer name")
        @RequestBody Map<String, String> request
    ) {
        String data = request.get("data");
        String printer = request.get("printer");
        
        if (data == null || printer == null) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", "Missing required parameters: data and printer"
            ));
        }
        
        try {
            // Create the lp command similar to the working example
            String command = String.format("echo -e \"%s\" | lp -d %s", 
                data.replace("\"", "\\\""), printer);
            
            Process process = Runtime.getRuntime().exec(new String[]{"bash", "-c", command});
            int exitCode = process.waitFor();
            
            if (exitCode == 0) {
                return ResponseEntity.ok(Map.of(
                    "message", "Receipt printed successfully",
                    "printer", printer
                ));
            } else {
                return ResponseEntity.internalServerError().body(Map.of(
                    "error", "Print command failed with exit code: " + exitCode
                ));
            }
            
        } catch (IOException | InterruptedException e) {
            return ResponseEntity.internalServerError().body(Map.of(
                "error", "Failed to execute print command: " + e.getMessage()
            ));
        }
    }
} 