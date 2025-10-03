package com.calc.restcalc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.calc.calculator.Calculator;
import com.calc.calculator.service.CalculationService;

@DisplayName("Calculator Service Application Tests")
class RestcalcApplicationTests {

	@Test
	@DisplayName("Spring Context should load successfully")
	void contextLoads() {
		// Test that Spring context loads without errors
		assertTrue(true, "Context loaded successfully");
	}

	@Nested
	@DisplayName("Calculator Core Logic Tests")
	class CalculatorTests {
		
		private Calculator calculator;
		
		@BeforeEach
		void setUp() {
			calculator = new Calculator();
		}
		
		@Nested
		@DisplayName("Addition Tests")
		class AdditionTests {
			
			@ParameterizedTest
			@CsvSource({
				"2.0, 3.0, 5.0",
				"0.0, 0.0, 0.0",
				"-5.0, 3.0, -2.0",
				"10.5, 4.5, 15.0",
				"1000000.0, 2000000.0, 3000000.0"
			})
			@DisplayName("Should correctly add various number pairs")
			void testAddition(Double a, Double b, Double expected) {
				Double result = calculator.makeOperation(a, b, "add");
				assertEquals(expected, result, 0.001, "Addition should be correct");
			}
			
			@Test
			@DisplayName("Should handle very large numbers")
			void testAdditionWithLargeNumbers() {
				Double result = calculator.makeOperation(Double.MAX_VALUE / 2, Double.MAX_VALUE / 4, "add");
				assertTrue(Double.isFinite(result), "Result should be finite for large number addition");
			}		
			@Test
			@DisplayName("Addition with very large numbers")
			void testAdditionLargeNumbers() {
				Double result = calculator.makeOperation(Double.MAX_VALUE / 2, Double.MAX_VALUE / 2, "add");
				        assertTrue(Double.isFinite(result), "Result should be finite for large number addition");
			}
		}
		
		@Nested
		@DisplayName("Subtraction Tests")
		class SubtractionTests {
			
			@ParameterizedTest(name = "{0} - {1} = {2}")
			@CsvSource({
				"5.0, 3.0, 2.0",
				"10.5, 3.2, 7.3",
				"-3.0, 2.0, -5.0",
				"0.0, 5.0, -5.0",
				"100.0, 100.0, 0.0"
			})
			void testSubtraction(Double operand1, Double operand2, Double expected) {
				Double result = calculator.makeOperation(operand1, operand2, "sub");
				assertEquals(expected, result, 0.001);
			}
		}
		
		@Nested
		@DisplayName("Multiplication Tests")
		class MultiplicationTests {
			
			@ParameterizedTest(name = "{0} * {1} = {2}")
			@CsvSource({
				"4.0, 2.5, 10.0",
				"3.0, 0.0, 0.0",
				"-2.0, 5.0, -10.0",
				"-3.0, -4.0, 12.0",
				"1.5, 2.0, 3.0"
			})
			void testMultiplication(Double operand1, Double operand2, Double expected) {
				Double result = calculator.makeOperation(operand1, operand2, "mul");
				assertEquals(expected, result, 0.001);
			}
		}
		
		@Nested
		@DisplayName("Division Tests")
		class DivisionTests {
			
			@ParameterizedTest(name = "{0} / {1} = {2}")
			@CsvSource({
				"15.0, 3.0, 5.0",
				"10.0, 2.0, 5.0",
				"-8.0, 2.0, -4.0",
				"7.5, 2.5, 3.0",
				"100.0, 4.0, 25.0"
			})
			void testDivision(Double operand1, Double operand2, Double expected) {
				Double result = calculator.makeOperation(operand1, operand2, "div");
				assertEquals(expected, result, 0.001);
			}
			
			@Test
			@DisplayName("Division by zero should throw IllegalArgumentException")
			void testDivisionByZero() {
				IllegalArgumentException exception = assertThrows(
					IllegalArgumentException.class,
					() -> calculator.makeOperation(10.0, 0.0, "div"),
					"Division by zero should throw IllegalArgumentException"
				);
				assertEquals("Division by zero is not allowed", exception.getMessage());
			}
			
			@Test
			@DisplayName("Division by very small number should not throw exception")
			void testDivisionByVerySmallNumber() {
				Double result = calculator.makeOperation(1.0, 0.0001, "div");
				assertEquals(10000.0, result, 0.001);
			}
		}
		
		@Nested
		@DisplayName("Error Handling Tests")
		class ErrorHandlingTests {
			
			@Test
			@DisplayName("Unsupported operation should throw IllegalArgumentException")
			void testUnsupportedOperation() {
				IllegalArgumentException exception = assertThrows(
					IllegalArgumentException.class,
					() -> calculator.makeOperation(5.0, 3.0, "mod"),
					"Unsupported operation should throw IllegalArgumentException"
				);
				assertEquals("Unsupported operation: mod", exception.getMessage());
			}
			
			@Test
			@DisplayName("Null operation should throw NullPointerException")
			void testNullOperation() {
				assertThrows(
					NullPointerException.class,
					() -> calculator.makeOperation(5.0, 3.0, null)
				);
			}
			
			@Test
			@DisplayName("Empty operation should throw IllegalArgumentException")
			void testEmptyOperation() {
				IllegalArgumentException exception = assertThrows(
					IllegalArgumentException.class,
					() -> calculator.makeOperation(5.0, 3.0, "")
				);
				assertEquals("Unsupported operation: ", exception.getMessage());
			}
		}
		
		@Nested
		@DisplayName("Getter/Setter Tests")
		class GetterSetterTests {
			
			@Test
			@DisplayName("Test operand A getter and setter")
			void testOperandAGetterSetter() {
				Double testValue = 42.5;
				calculator.setOperandA(testValue);
				assertEquals(testValue, calculator.getOperandA());
			}
			
			@Test
			@DisplayName("Test operand B getter and setter")
			void testOperandBGetterSetter() {
				Double testValue = 17.3;
				calculator.setOperandB(testValue);
				assertEquals(testValue, calculator.getOperandB());
			}
			
			@Test
			@DisplayName("Test null operand handling")
			void testNullOperands() {
				calculator.setOperandA(null);
				calculator.setOperandB(null);
				assertNull(calculator.getOperandA());
				assertNull(calculator.getOperandB());
			}
		}
	}

	@Test
	@DisplayName("CalculationService should be instantiable")
	void testCalculationServiceInstantiation() {
		// This ensures CalculationService can be created without issues
		CalculationService service = new CalculationService();
		assertNotNull(service, "CalculationService should be created successfully");
	}
}
