package com.calc.calculator.dto;

public class CalculationResponse {
    private String requestId;
    private Double result;
    private boolean success;
    private String errorMessage;

    // Default constructor
    public CalculationResponse() {}

    // Success constructor
    public CalculationResponse(String requestId, Double result) {
        this.requestId = requestId;
        this.result = result;
        this.success = true;
    }

    // Error constructor
    public CalculationResponse(String requestId, String errorMessage) {
        this.requestId = requestId;
        this.errorMessage = errorMessage;
        this.success = false;
    }

    // Getters and Setters
    public String getRequestId() { return requestId; }
    public void setRequestId(String requestId) { this.requestId = requestId; }

    public Double getResult() { return result; }
    public void setResult(Double result) { this.result = result; }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
}
