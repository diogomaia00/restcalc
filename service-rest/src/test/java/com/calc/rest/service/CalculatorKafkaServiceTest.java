package com.calc.rest.service;

import java.lang.reflect.Field;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import com.calc.rest.dto.CalculationRequest;
import com.calc.rest.dto.CalculationResponse;

@ExtendWith(MockitoExtension.class)
@DisplayName("CalculatorKafkaService Tests")
class CalculatorKafkaServiceTest {
    
    @Mock
    private KafkaTemplate<String, CalculationRequest> kafkaTemplate;
    
    private CalculatorKafkaService calculatorKafkaService;
    
    @BeforeEach
    void setUp() {
        calculatorKafkaService = new CalculatorKafkaService();
        
        // Use reflection to inject the mock KafkaTemplate
        try {
            Field kafkaField = CalculatorKafkaService.class.getDeclaredField("kafkaTemplate");
            kafkaField.setAccessible(true);
            kafkaField.set(calculatorKafkaService, kafkaTemplate);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Failed to inject mock KafkaTemplate: " + e.getMessage());
        }
    }
    
    @Test
    @DisplayName("Handle valid calculation response should complete future")
    void testHandleValidCalculationResponse() throws Exception {
        // Given
        String requestId = "test-123";
        Double result = 8.0;
        CalculationResponse response = new CalculationResponse(requestId, result);
        
        // Manually add to pending requests using reflection
        Field pendingField = CalculatorKafkaService.class.getDeclaredField("pendingRequests");
        pendingField.setAccessible(true);
        @SuppressWarnings("unchecked")
        ConcurrentHashMap<String, CompletableFuture<CalculationResponse>> pendingRequests = 
            (ConcurrentHashMap<String, CompletableFuture<CalculationResponse>>) pendingField.get(calculatorKafkaService);
        
        CompletableFuture<CalculationResponse> testFuture = new CompletableFuture<>();
        pendingRequests.put(requestId, testFuture);
        
        // When
        calculatorKafkaService.handleCalculationResponse(response);
        
        // Then
        CalculationResponse completedResponse = testFuture.get(1, TimeUnit.SECONDS);
        assertEquals(requestId, completedResponse.getRequestId());
        assertEquals(result, completedResponse.getResult());
        
        // Verify the request was removed from pending requests
        assertFalse(pendingRequests.containsKey(requestId));
    }
    
    @Test
    @DisplayName("Handle response for non-existent request should not throw exception")
    void testHandleResponseForNonExistentRequest() {
        // Given
        String requestId = "non-existent-123";
        CalculationResponse response = new CalculationResponse(requestId, 42.0);
        
        // When & Then - should not throw exception
        assertDoesNotThrow(() -> calculatorKafkaService.handleCalculationResponse(response));
    }
    
    @Test
    @DisplayName("Kafka send failure should return error response")
    void testKafkaSendFailure() {
        // Given
        Double operand1 = 5.0;
        Double operand2 = 3.0;
        String operation = "add";
        
        // Mock Kafka send to throw exception
        when(kafkaTemplate.send(anyString(), anyString(), any(CalculationRequest.class)))
            .thenThrow(new RuntimeException("Kafka connection failed"));
        
        // When
        CalculationResponse result = calculatorKafkaService.performCalculation(operand1, operand2, operation);
        
        // Then
        assertNotNull(result);
        assertNotNull(result.getRequestId());
        assertTrue(result.getErrorMessage().contains("Kafka connection failed"));
    }
}