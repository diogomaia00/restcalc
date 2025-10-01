package com.calc.calculator;

public class Calculator {
    private Double operand_a;
    private Double operand_b;

    // gets
    public Double getOperandA() {
        return operand_a;
    }
    public Double getOperandB() {
        return operand_b;
    }
    
    // sets
    public void setOperandA(Double op_a) {
        this.operand_a = op_a;
    }
    public void setOperandB(Double op_b) {
        this.operand_b = op_b;
    }

    // operations
    public Double makeOperation(Double op_a, Double op_b, String operation) {
        Double result;
        switch (operation) {
            case "add" -> result = op_a + op_b;
            case "sub" -> result = op_a - op_b;
            case "mul" -> result = op_a * op_b;
            case "div" -> {
                if (op_b != 0.0) {
                    result = op_a / op_b;
                } else {
                    throw new IllegalArgumentException("Division by zero is not allowed");
                }
            }
            default -> throw new IllegalArgumentException("Unsupported operation: " + operation);
        }
        return result;
    }
}
