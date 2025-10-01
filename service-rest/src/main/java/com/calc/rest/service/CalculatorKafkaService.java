package com.calc.rest.service;


import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.calc.rest.dto.CalculationRequest;
import com.calc.rest.dto.CalculationResponse;

@Service
public class CalculatorKafkaService {

    @Autowired
    private KafkaTemplate<String, CalculationRequest> kafkaTemplate;

    // Store pending requests waiting for responses
    private final ConcurrentHashMap<String, CompletableFuture<CalculationResponse>> pendingRequests = new ConcurrentHashMap<>();

    private static final String REQUEST_TOPIC = "calculation-requests";
    private static final String RESPONSE_TOPIC = "calculation-responses";

    public CalculationResponse performCalculation(Double operand1, Double operand2, String operation) {
        String requestId = UUID.randomUUID().toString();
        
        // Create the request
        CalculationRequest request = new CalculationRequest(requestId, operand1, operand2, operation);
        
        // Create a future to wait for the response
        CompletableFuture<CalculationResponse> future = new CompletableFuture<>();
        pendingRequests.put(requestId, future);
        
        try {
            // Send the request to Kafka
            System.out.println("📤 REST service sending request: " + requestId + 
                              " - " + operand1 + " " + operation + " " + operand2);
            
            // Send message and log the result
            var sendResult = kafkaTemplate.send(REQUEST_TOPIC, requestId, request);
            sendResult.whenComplete((result, ex) -> {
                if (ex == null) {
                    System.out.println("✅ Message sent successfully to topic: " + REQUEST_TOPIC);
                    System.out.println("📊 Message details: partition=" + result.getRecordMetadata().partition() + 
                                     ", offset=" + result.getRecordMetadata().offset());
                } else {
                    System.out.println("❌ Failed to send message: " + ex.getMessage());
                }
            });
            
            // Force flush to ensure message is sent immediately
            kafkaTemplate.flush();
            
            // Wait for response (with timeout)
            return future.get(5, TimeUnit.SECONDS);
            
        } catch (Exception e) {
            // Clean up and return error response
            pendingRequests.remove(requestId);
            return new CalculationResponse(requestId, "Request timeout or error: " + e.getMessage());
        }
    }

    @KafkaListener(topics = RESPONSE_TOPIC, groupId = "rest-service-group")
    public void handleCalculationResponse(CalculationResponse response) {
        System.out.println("📥 REST service received response: " + response.getRequestId() + " = " + response.getResult());
        CompletableFuture<CalculationResponse> future = pendingRequests.remove(response.getRequestId());
        if (future != null) {
            future.complete(response);
        } else {
            System.out.println("⚠️ No pending request found for: " + response.getRequestId());
        }
    }
}
