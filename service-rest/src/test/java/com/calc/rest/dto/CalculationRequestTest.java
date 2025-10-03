package com.calc.rest.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("CalculationRequest DTO Tests")
class CalculationRequestTest {
    
    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {
        
        @Test
        @DisplayName("Full constructor should set all fields correctly")
        void testFullConstructor() {
            // Given
            String requestId = "test-123";
            Double operand1 = 5.0;
            Double operand2 = 3.0;
            String operation = "add";
            
            // When
            CalculationRequest request = new CalculationRequest(requestId, operand1, operand2, operation);
            
            // Then
            assertEquals(requestId, request.getRequestId());
            assertEquals(operand1, request.getOperand1());
            assertEquals(operand2, request.getOperand2());
            assertEquals(operation, request.getOperation());
        }
        
        @Test
        @DisplayName("Default constructor should create empty object")
        void testDefaultConstructor() {
            // When
            CalculationRequest request = new CalculationRequest();
            
            // Then
            assertNull(request.getRequestId());
            assertNull(request.getOperand1());
            assertNull(request.getOperand2());
            assertNull(request.getOperation());
        }
    }
    
    @Nested
    @DisplayName("Getter and Setter Tests")
    class GetterSetterTests {
        
        @Test
        @DisplayName("RequestId getter and setter should work correctly")
        void testRequestIdGetterSetter() {
            // Given
            CalculationRequest request = new CalculationRequest();
            String requestId = "setter-test-123";
            
            // When
            request.setRequestId(requestId);
            
            // Then
            assertEquals(requestId, request.getRequestId());
        }
        
        @Test
        @DisplayName("Operand1 getter and setter should work correctly")
        void testOperand1GetterSetter() {
            // Given
            CalculationRequest request = new CalculationRequest();
            Double operand1 = 42.5;
            
            // When
            request.setOperand1(operand1);
            
            // Then
            assertEquals(operand1, request.getOperand1());
        }
        
        @Test
        @DisplayName("Operand2 getter and setter should work correctly")
        void testOperand2GetterSetter() {
            // Given
            CalculationRequest request = new CalculationRequest();
            Double operand2 = 17.3;
            
            // When
            request.setOperand2(operand2);
            
            // Then
            assertEquals(operand2, request.getOperand2());
        }
        
        @Test
        @DisplayName("Operation getter and setter should work correctly")
        void testOperationGetterSetter() {
            // Given
            CalculationRequest request = new CalculationRequest();
            String operation = "multiply";
            
            // When
            request.setOperation(operation);
            
            // Then
            assertEquals(operation, request.getOperation());
        }
        
        @Test
        @DisplayName("Null values should be handled correctly")
        void testNullValues() {
            // Given
            CalculationRequest request = new CalculationRequest();
            
            // When
            request.setRequestId(null);
            request.setOperand1(null);
            request.setOperand2(null);
            request.setOperation(null);
            
            // Then
            assertNull(request.getRequestId());
            assertNull(request.getOperand1());
            assertNull(request.getOperand2());
            assertNull(request.getOperation());
        }
    }
    
    @Nested
    @DisplayName("Edge Case Tests")
    class EdgeCaseTests {
        
        @Test
        @DisplayName("Empty string values should be preserved")
        void testEmptyStringValues() {
            // Given
            CalculationRequest request = new CalculationRequest("", 0.0, 0.0, "");
            
            // Then
            assertEquals("", request.getRequestId());
            assertEquals("", request.getOperation());
            assertEquals(0.0, request.getOperand1());
            assertEquals(0.0, request.getOperand2());
        }
        
        @Test
        @DisplayName("Very large numbers should be handled")
        void testLargeNumbers() {
            // Given
            Double largeNumber1 = Double.MAX_VALUE;
            Double largeNumber2 = Double.MIN_VALUE;
            CalculationRequest request = new CalculationRequest("large-test", largeNumber1, largeNumber2, "add");
            
            // Then
            assertEquals(largeNumber1, request.getOperand1());
            assertEquals(largeNumber2, request.getOperand2());
        }
        
        @Test
        @DisplayName("Special double values should be handled")
        void testSpecialDoubleValues() {
            // Given
            CalculationRequest request = new CalculationRequest();
            
            // Test positive infinity
            request.setOperand1(Double.POSITIVE_INFINITY);
            assertEquals(Double.POSITIVE_INFINITY, request.getOperand1());
            
            // Test negative infinity
            request.setOperand2(Double.NEGATIVE_INFINITY);
            assertEquals(Double.NEGATIVE_INFINITY, request.getOperand2());
            
            // Test NaN
            request.setOperand1(Double.NaN);
            assertTrue(Double.isNaN(request.getOperand1()));
        }
    }
}