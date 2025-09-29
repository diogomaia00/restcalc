public class Calculator {
    private Float operand_a;
    private Float operand_b;

    public Float getOperandA() {
        return operand_a;
    }
    public Float getOperandB() {
        return operand_b;
    }
    
    public void setOperandA(Float op_a) {
        this.operand_a = op_a;
    }
    public void setOperandB(Float op_b) {
        this.operand_b = op_b;
    }

    public Float makeOperation(Float op_a, Float op_b, String operation) {
        Float result = 0.0f;
        switch (operation) {
            case "add" -> result = op_a + op_b;
            case "sub" -> result = op_a - op_b;
            case "mul" -> result = op_a * op_b;
            case "div" -> {
                if (op_b != 0) {
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
