package com.calc.restcalc;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Calculator {
    private BigDecimal operand_a;
    private BigDecimal operand_b;

    // gets
    public BigDecimal getOperandA() {
        return operand_a;
    }
    public BigDecimal getOperandB() {
        return operand_b;
    }
    
    // sets
    public void setOperandA(BigDecimal op_a) {
        this.operand_a = op_a;
    }
    public void setOperandB(BigDecimal op_b) {
        this.operand_b = op_b;
    }

    // operations
    public BigDecimal makeOperation(BigDecimal op_a, BigDecimal op_b, String operation) {
        BigDecimal result;
        switch (operation) {
            case "add" -> result = op_a.add(op_b);
            case "sub" -> result = op_a.subtract(op_b);
            case "mul" -> result = op_a.multiply(op_b);
            case "div" -> {
                if (op_b.compareTo(BigDecimal.ZERO) != 0) {
                    result = op_a.divide(op_b, 10, RoundingMode.HALF_UP);
                } else {
                    throw new IllegalArgumentException("Division by zero is not allowed");
                }
            }
            default -> throw new IllegalArgumentException("Unsupported operation: " + operation);
        }
        return result;
    }
}
