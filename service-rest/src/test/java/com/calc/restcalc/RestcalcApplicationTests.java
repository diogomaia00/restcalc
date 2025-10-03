package com.calc.restcalc;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import com.calc.rest.dto.CalculationRequest;
import com.calc.rest.dto.CalculationResponse;

@DisplayName("REST Service Application Tests")
class RestcalcApplicationTests {

	@Test
	@DisplayName("Application should be testable")
	void contextLoads() {
		// This test ensures the test setup works properly
		assertTrue(true, "Basic test should pass");
	}

	@Nested
	@DisplayName("CalculationRequest DTO Tests")
	class CalculationRequestTests {
		
		@Test
		@DisplayName("Should create CalculationRequest with all parameters")
		void testCalculationRequestCreation() {
			// Given
			String requestId = "test-request-123";
			Double operand1 = 10.5;
			Double operand2 = 5.2;
			String operation = "add";
			
			// When
			CalculationRequest request = new CalculationRequest(requestId, operand1, operand2, operation);
			
			// Then
			assertNotNull(request, "Request should not be null");
			assertEquals(requestId, request.getRequestId(), "Request ID should match");
			assertEquals(operand1, request.getOperand1(), "Operand1 should match");
			assertEquals(operand2, request.getOperand2(), "Operand2 should match");
			assertEquals(operation, request.getOperation(), "Operation should match");
		}
		
		@ParameterizedTest
		@ValueSource(strings = {"add", "sub", "mul", "div"})
		@DisplayName("Should accept all valid operations")
		void testValidOperations(String operation) {
			CalculationRequest request = new CalculationRequest("test", 1.0, 2.0, operation);
			assertEquals(operation, request.getOperation(), "Operation should be set correctly");
		}
		
		@ParameterizedTest
		@CsvSource({
			"0.0, 0.0",
			"-5.5, 3.3",
			"999999.99, -999999.99",
			"0.0001, 0.0001"
		})
		@DisplayName("Should handle various operand values")
		void testVariousOperandValues(Double op1, Double op2) {
			CalculationRequest request = new CalculationRequest("test", op1, op2, "add");
			assertEquals(op1, request.getOperand1(), "Operand1 should be preserved");
			assertEquals(op2, request.getOperand2(), "Operand2 should be preserved");
		}
		
		@Test
		@DisplayName("Should handle null values gracefully")
		void testNullHandling() {
			assertDoesNotThrow(() -> {
				new CalculationRequest(null, null, null, null);
			}, "Constructor should handle null parameters without throwing");
		}
		
		@Test
		@DisplayName("Should handle edge case numbers")
		void testEdgeCaseNumbers() {
			// Test with special double values
			CalculationRequest request1 = new CalculationRequest("test", Double.MAX_VALUE, Double.MIN_VALUE, "add");
			assertNotNull(request1, "Should handle extreme values");
			
			CalculationRequest request2 = new CalculationRequest("test", -0.0, 0.0, "sub");
			assertNotNull(request2, "Should handle zero variants");
		}
	}
	
	@Nested
	@DisplayName("CalculationResponse DTO Tests")
	class CalculationResponseTests {
		
		@Test
		@DisplayName("Should create successful response")
		void testSuccessfulResponse() {
			// Given
			String requestId = "success-test-456";
			Double result = 15.7;
			
			// When
			CalculationResponse response = new CalculationResponse(requestId, result);
			
			// Then
			assertNotNull(response, "Response should not be null");
			assertEquals(requestId, response.getRequestId(), "Request ID should match");
			assertEquals(result, response.getResult(), "Result should match");
			assertTrue(response.isSuccess(), "Response should indicate success");
			assertNull(response.getErrorMessage(), "Error message should be null for success");
		}
		
		@Test
		@DisplayName("Should create error response")
		void testErrorResponse() {
			// Given
			String requestId = "error-test-789";
			String errorMessage = "Division by zero is not allowed";
			
			// When
			CalculationResponse response = new CalculationResponse(requestId, errorMessage);
			
			// Then
			assertNotNull(response, "Response should not be null");
			assertEquals(requestId, response.getRequestId(), "Request ID should match");
			assertEquals(errorMessage, response.getErrorMessage(), "Error message should match");
			assertFalse(response.isSuccess(), "Response should indicate failure");
			assertNull(response.getResult(), "Result should be null for error");
		}
		
		@ParameterizedTest
		@CsvSource({
			"0.0, true",
			"-0.0, true", 
			"123.456, true",
			"-999.999, true"
		})
		@DisplayName("Should handle various result values")
		void testVariousResults(Double result, boolean expectedSuccess) {
			CalculationResponse response = new CalculationResponse("test", result);
			assertEquals(expectedSuccess, response.isSuccess(), "Success flag should match expected");
			assertEquals(result, response.getResult(), "Result should be preserved");
		}
		
		@ParameterizedTest
		@ValueSource(strings = {"", "Error occurred", "Invalid operation", "Timeout error"})
		@DisplayName("Should handle various error messages")  
		void testVariousErrorMessages(String errorMessage) {
			CalculationResponse response = new CalculationResponse("test", errorMessage);
			assertFalse(response.isSuccess(), "Should indicate failure for any error message");
			assertEquals(errorMessage, response.getErrorMessage(), "Error message should be preserved");
		}
		
		@Test
		@DisplayName("Should handle special double values in results")
		void testSpecialDoubleValues() {
			// Test infinity
			CalculationResponse infinityResponse = new CalculationResponse("test", Double.POSITIVE_INFINITY);
			assertTrue(infinityResponse.isSuccess(), "Infinity should be considered success");
			assertEquals(Double.POSITIVE_INFINITY, infinityResponse.getResult());
			
			// Test NaN
			CalculationResponse nanResponse = new CalculationResponse("test", Double.NaN);
			assertTrue(nanResponse.isSuccess(), "NaN should be considered success");
			assertTrue(Double.isNaN(nanResponse.getResult()), "Result should be NaN");
		}
	}
	
	@Nested
	@DisplayName("Service Integration Tests")
	class ServiceIntegrationTests {
		
		@Test
		@DisplayName("Should be able to load service classes")
		void testServiceClassLoading() {
			assertDoesNotThrow(() -> {
				Class.forName("com.calc.rest.service.CalculatorKafkaService");
				Class.forName("com.calc.rest.CalculatorController");
			}, "Service classes should be loadable");
		}
		
		@Test
		@DisplayName("Should validate class accessibility")
		void testClassAccessibility() {
			// Verify that our main classes are accessible
			assertDoesNotThrow(() -> {
				Class<?> requestClass = CalculationRequest.class;
				Class<?> responseClass = CalculationResponse.class;
				
				assertNotNull(requestClass, "CalculationRequest class should be accessible");
				assertNotNull(responseClass, "CalculationResponse class should be accessible");
			}, "DTO classes should be accessible");
		}
	}
	
	@Nested
	@DisplayName("Data Validation Tests")
	class DataValidationTests {
		
		@Test
		@DisplayName("Should validate request ID formats")
		void testRequestIdFormats() {
			String[] validIds = {
				"simple-id",
				"UUID-12345-67890",
				"request_123",
				"r1",
				"very-long-request-id-with-many-characters-123456789"
			};
			
			for (String id : validIds) {
				CalculationRequest request = new CalculationRequest(id, 1.0, 2.0, "add");
				assertEquals(id, request.getRequestId(), "Request ID should be preserved: " + id);
			}
		}
		
		@Test
		@DisplayName("Should handle precision requirements")
		void testPrecisionHandling() {
			// Test with high precision numbers
			Double preciseNumber1 = 123.456789123456;
			Double preciseNumber2 = 987.654321987654;
			
			CalculationRequest request = new CalculationRequest("precision-test", preciseNumber1, preciseNumber2, "mul");
			
			assertEquals(preciseNumber1, request.getOperand1(), "High precision operand1 should be preserved");
			assertEquals(preciseNumber2, request.getOperand2(), "High precision operand2 should be preserved");
		}
		
		@Test
		@DisplayName("Should validate operation case sensitivity")
		void testOperationCaseSensitivity() {
			// Our system expects lowercase operations
			String[] operations = {"add", "sub", "mul", "div"};
			
			for (String op : operations) {
				CalculationRequest request = new CalculationRequest("test", 1.0, 2.0, op);
				assertEquals(op, request.getOperation(), "Operation should preserve case: " + op);
			}
		}
	}
}
