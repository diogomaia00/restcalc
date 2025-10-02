package com.calc.calculator.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.calc.calculator.Calculator;
import com.calc.calculator.dto.CalculationRequest;
import com.calc.calculator.dto.CalculationResponse;

@Service
public class CalculationService {

    @Autowired 
    private KafkaTemplate<String, CalculationResponse> kafkaTemplate;
    private static final String RESPONSE_TOPIC = "calculation-responses";

    private final Calculator calculator = new Calculator();

    @KafkaListener(topics = "calculation-requests", groupId = "calculator-service-group")
    public void handleCalculationRequest(CalculationRequest request) {
      
        try {
            // Perform the calculation
            var result = calculator.makeOperation(request.getOperand1(), request.getOperand2(), request.getOperation());
            
            // Send successful response
            CalculationResponse response = new CalculationResponse(request.getRequestId(), result);
            kafkaTemplate.send(RESPONSE_TOPIC, request.getRequestId(), response);
            
        } catch (Exception e) {
            // Send error response
            CalculationResponse response = new CalculationResponse(request.getRequestId(), e.getMessage());
            kafkaTemplate.send(RESPONSE_TOPIC, request.getRequestId(), response);
        }
    }
}
