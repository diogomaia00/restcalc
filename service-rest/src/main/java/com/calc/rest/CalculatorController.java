package com.calc.rest;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.calc.rest.dto.CalculationResponse;
import com.calc.rest.service.CalculatorKafkaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Calculator", description = "RESTful Calculator API for basic arithmetic operations")
public class CalculatorController {
    
    @Autowired
    private CalculatorKafkaService calculatorService;

    // WELCOMING
    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> welcoming() {
        String message = "Possible endpoints: /add, /sub, /mul, /div with parameters op1 and op2. Example: /add?op1=5&op2=2.3";
        String welcomingRequestId = java.util.UUID.randomUUID().toString();

        return ResponseEntity.ok()
            .header("request-ID", welcomingRequestId)
            .body(Map.of("", message));
    }

    // ADDITION
    @GetMapping("/add")
    @Operation(
        summary = "Addition operation", 
        description = "Performs addition of two numbers (op1 + op2)"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Addition completed successfully",
            headers = @Header(name = "request-ID", description = "Unique request identifier", 
                schema = @Schema(type = "string", format = "uuid")),
            content = @Content(mediaType = "application/json",
                examples = @ExampleObject(value = "{\"result\": 3.8}"))),
        @ApiResponse(responseCode = "400", description = "Bad request - invalid parameters",
            headers = @Header(name = "request-ID", description = "Unique request identifier", 
                schema = @Schema(type = "string", format = "uuid")),
            content = @Content(mediaType = "application/json",
                examples = @ExampleObject(value = "{\"message\": \"Invalid parameters\"}"))),
        @ApiResponse(responseCode = "500", description = "Internal server error",
            headers = @Header(name = "request-ID", description = "Unique request identifier", 
                schema = @Schema(type = "string", format = "uuid")))
    })
    public ResponseEntity<Map<String, Object>> addition(
        @Parameter(description = "First operand", example = "1.5", required = true)
        @RequestParam(value= "op1", required=true) Double op1,
        @Parameter(description = "Second operand", example = "2.3", required = true)
        @RequestParam(value= "op2", required=true) Double op2
    ) {
        CalculationResponse response = calculatorService.performCalculation(op1, op2, "add");
        if (response.isSuccess()) {
            return ResponseEntity.ok()
                .header("request-ID", response.getRequestId())
                .body(Map.of("result", response.getResult()));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .header("request-ID", response.getRequestId())
                .body(Map.of("message", response.getErrorMessage()));
        }
    }

    // SUBTRACTION
    @GetMapping("/sub")
    @Operation(
        summary = "Subtraction operation", 
        description = "Performs subtraction of two numbers (op1 - op2)"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Subtraction completed successfully",
             headers = @Header(name = "request-ID", description = "Unique request identifier", 
                schema = @Schema(type = "string", format = "uuid")),
            content = @Content(mediaType = "application/json",
                examples = @ExampleObject(value = "{\"result\": 3.8}"))),
        @ApiResponse(responseCode = "400", description = "Bad request - invalid parameters",
            headers = @Header(name = "request-ID", description = "Unique request identifier", 
                schema = @Schema(type = "string", format = "uuid")),
            content = @Content(mediaType = "application/json",
                examples = @ExampleObject(value = "{\"message\": \"Invalid parameters\"}"))),
        @ApiResponse(responseCode = "500", description = "Internal server error",
            headers = @Header(name = "request-ID", description = "Unique request identifier", 
                schema = @Schema(type = "string", format = "uuid")))
    })
    public ResponseEntity<Map<String, Object>> subtraction(
        @Parameter(description = "First operand", example = "10.5", required = true)
        @RequestParam(value= "op1", required=true) Double op1,
        @Parameter(description = "Second operand", example = "3.2", required = true)
        @RequestParam(value= "op2", required=true) Double op2
    ) {
        CalculationResponse response = calculatorService.performCalculation(op1, op2, "sub");
        if (response.isSuccess()) {
            return ResponseEntity.ok()
                .header("request-ID", response.getRequestId())
                .body(Map.of("result", response.getResult()));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .header("request-ID", response.getRequestId())
                .body(Map.of("message", response.getErrorMessage()));
        }
    }

    // MULTIPLICATION
    @GetMapping("/mul")
    @Operation(
        summary = "Multiplication operation", 
        description = "Performs multiplication of two numbers (op1 * op2)"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Multiplication completed successfully",
             headers = @Header(name = "request-ID", description = "Unique request identifier", 
                schema = @Schema(type = "string", format = "uuid")),
            content = @Content(mediaType = "application/json",
                examples = @ExampleObject(value = "{\"result\": 3.8}"))),
        @ApiResponse(responseCode = "400", description = "Bad request - invalid parameters",
            headers = @Header(name = "request-ID", description = "Unique request identifier", 
                schema = @Schema(type = "string", format = "uuid")),
            content = @Content(mediaType = "application/json",
                examples = @ExampleObject(value = "{\"message\": \"Invalid parameters\"}"))),
        @ApiResponse(responseCode = "500", description = "Internal server error",
            headers = @Header(name = "request-ID", description = "Unique request identifier", 
                schema = @Schema(type = "string", format = "uuid")))
    })
    public ResponseEntity<Map<String, Object>> multiplication(
        @Parameter(description = "First operand", example = "4.0", required = true)
        @RequestParam(value= "op1", required=true) Double op1,
        @Parameter(description = "Second operand", example = "2.5", required = true)
        @RequestParam(value= "op2", required=true) Double op2
    ) {
        CalculationResponse response = calculatorService.performCalculation(op1, op2, "mul");
        if (response.isSuccess()) {
            return ResponseEntity.ok()
                .header("request-ID", response.getRequestId())
                .body(Map.of("result", response.getResult()));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .header("request-ID", response.getRequestId())
                .body(Map.of("message", response.getErrorMessage()));
        }
    }

    // DIVISION
    @GetMapping("/div")
    @Operation(
        summary = "Division operation", 
        description = "Performs division of two numbers (op1 / op2). Note: op2 cannot be zero."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Division completed successfully",
             headers = @Header(name = "request-ID", description = "Unique request identifier", 
                schema = @Schema(type = "string", format = "uuid")),
            content = @Content(mediaType = "application/json",
                examples = @ExampleObject(value = "{\"result\": 3.8}"))),
        @ApiResponse(responseCode = "400", description = "Bad request - invalid parameters or division by zero",
            headers = @Header(name = "request-ID", description = "Unique request identifier", 
                schema = @Schema(type = "string", format = "uuid")),
            content = @Content(mediaType = "application/json",
                examples = @ExampleObject(value = "{\"message\": \"Invalid parameters\"}"))),
        @ApiResponse(responseCode = "500", description = "Internal server error",
            headers = @Header(name = "request-ID", description = "Unique request identifier", 
                schema = @Schema(type = "string", format = "uuid")))
    })
    public ResponseEntity<Map<String, Object>> division(
        @Parameter(description = "First operand", example = "15.0", required = true)
        @RequestParam(value= "op1", required=true) Double op1,
        @Parameter(description = "Second operand - cannot be zero", example = "3.0", required = true)
        @RequestParam(value= "op2", required=true) Double op2
    ) {
        CalculationResponse response = calculatorService.performCalculation(op1, op2, "div");
        if (response.isSuccess()) {
            return ResponseEntity.ok()
                .header("request-ID", response.getRequestId())
                .body(Map.of("result", response.getResult()));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .header("request-ID", response.getRequestId())
                .body(Map.of("message", response.getErrorMessage()));
        }
    }

    // ERROR HANDLER (parameter conversion errors)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, String>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        
        String message = String.format("Invalid value '%s' for parameter '%s'. Expected a valid number.", ex.getValue(), ex.getName());
        String errorRequestId = java.util.UUID.randomUUID().toString();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .header("request-ID", errorRequestId)
            .body(Map.of("message", message));
    }
}
