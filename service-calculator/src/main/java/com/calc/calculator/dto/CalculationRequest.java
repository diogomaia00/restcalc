package com.calc.calculator.dto;

public class CalculationRequest {
    private String requestId;
    private Double operand1;
    private Double operand2;
    private String operation;

    // Default constructor
    public CalculationRequest() {}

    // Constructor
    public CalculationRequest(String requestId, Double operand1, Double operand2, String operation) {
        this.requestId = requestId;
        this.operand1 = operand1;
        this.operand2 = operand2;
        this.operation = operation;
    }

    // Getters and Setters
    public String getRequestId() { return requestId; }
    public void setRequestId(String requestId) { this.requestId = requestId; }

    public Double getOperand1() { return operand1; }
    public void setOperand1(Double operand1) { this.operand1 = operand1; }

    public Double getOperand2() { return operand2; }
    public void setOperand2(Double operand2) { this.operand2 = operand2; }

    public String getOperation() { return operation; }
    public void setOperation(String operation) { this.operation = operation; }
}
