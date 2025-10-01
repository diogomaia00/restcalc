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

@RestController
public class CalculatorController {
    
    @Autowired
    private CalculatorKafkaService calculatorService;

    @GetMapping("/add")
    public ResponseEntity<Map<String, Object>> addition(
        @RequestParam(value= "op1", required=true) Double op1,
        @RequestParam(value= "op2", required=true) Double op2
    ) {
        CalculationResponse response = calculatorService.performCalculation(op1, op2, "add");
        if (response.isSuccess()) {
            return ResponseEntity.ok(Map.of("result", response.getResult()));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("message", response.getErrorMessage()));
        }
    }

    @GetMapping("/sub")
    public ResponseEntity<Map<String, Object>> subtraction(
        @RequestParam(value= "op1", required=true) Double op1,
        @RequestParam(value= "op2", required=true) Double op2
    ) {
        CalculationResponse response = calculatorService.performCalculation(op1, op2, "sub");
        if (response.isSuccess()) {
            return ResponseEntity.ok(Map.of("result", response.getResult()));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("message", response.getErrorMessage()));
        }
    }

    @GetMapping("/mul")
    public ResponseEntity<Map<String, Object>> multiplication(
        @RequestParam(value= "op1", required=true) Double op1,
        @RequestParam(value= "op2", required=true) Double op2
    ) {
        CalculationResponse response = calculatorService.performCalculation(op1, op2, "mul");
        if (response.isSuccess()) {
            return ResponseEntity.ok(Map.of("result", response.getResult()));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("message", response.getErrorMessage()));
        }
    }

    @GetMapping("/div")
    public ResponseEntity<Map<String, Object>> division(
        @RequestParam(value= "op1", required=true) Double op1,
        @RequestParam(value= "op2", required=true) Double op2
    ) {
        CalculationResponse response = calculatorService.performCalculation(op1, op2, "div");
        if (response.isSuccess()) {
            return ResponseEntity.ok(Map.of("result", response.getResult()));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("message", response.getErrorMessage()));
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
