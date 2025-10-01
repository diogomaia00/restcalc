package com.calc.restcalc;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestController
public class CalculatorController {
    private final Calculator c = new Calculator();

    @GetMapping("/add")
    public ResponseEntity<Map<String, Object>> addition(
        @RequestParam(value= "op1", required=true) BigDecimal op1,
        @RequestParam(value= "op2", required=true) BigDecimal op2
    ) {
        BigDecimal result = c.makeOperation(op1, op2, "add");
        return ResponseEntity.ok(Map.of("result", result));
    }

    @GetMapping("/sub")
    public ResponseEntity<Map<String, Object>> subtraction(
        @RequestParam(value= "op1", required=true) BigDecimal op1,
        @RequestParam(value= "op2", required=true) BigDecimal op2
    ) {
        BigDecimal result = c.makeOperation(op1, op2, "sub");
        return ResponseEntity.ok(Map.of("result", result));
    }

    @GetMapping("/mul")
    public ResponseEntity<Map<String, Object>> multiplication(
        @RequestParam(value= "op1", required=true) BigDecimal op1,
        @RequestParam(value= "op2", required=true) BigDecimal op2
    ) {
        BigDecimal result = c.makeOperation(op1, op2, "mul");
        return ResponseEntity.ok(Map.of("result", result));
    }

    @GetMapping("/div")
    public ResponseEntity<Map<String, Object>> division(
        @RequestParam(value= "op1", required=true) BigDecimal op1,
        @RequestParam(value= "op2", required=true) BigDecimal op2
    ) {
        try {
            BigDecimal result = c.makeOperation(op1, op2, "div");
            return ResponseEntity.ok(Map.of("result", result));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("message", e.getMessage()));
        }
    }

    // Error handler: parameter conversion errors
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, String>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        
        String message = String.format("Invalid value '%s' for parameter '%s'. Expected a valid number.", ex.getValue(), ex.getName());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(Map.of("message", message));
    }
}
