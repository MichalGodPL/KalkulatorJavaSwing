import java.util.Stack;

public class CalculatorLogic {
    public static String calculate(String expression) {
        expression = expression.replace("×", "*").replace("÷", "/");
        try {
            Stack<Double> values = new Stack<>();
            Stack<Character> ops = new Stack<>();
            for (int i = 0; i < expression.length(); i++) {
                if (Character.isDigit(expression.charAt(i))) {
                    StringBuilder buffer = new StringBuilder();
                    while (i < expression.length() && (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.')) {
                        buffer.append(expression.charAt(i++));
                    }
                    values.push(Double.parseDouble(buffer.toString()));
                    i--;
                } else if (expression.charAt(i) == '(') {
                    ops.push(expression.charAt(i));
                } else if (expression.charAt(i) == ')') {
                    while (ops.peek() != '(') values.push(applyOp(ops.pop(), values.pop(), values.pop()));
                    ops.pop();
                } else if ("+-*/".indexOf(expression.charAt(i)) >= 0) {
                    while (!ops.isEmpty() && hasPrecedence(expression.charAt(i), ops.peek())) {
                        values.push(applyOp(ops.pop(), values.pop(), values.pop()));
                    }
                    ops.push(expression.charAt(i));
                }
            }
            while (!ops.isEmpty()) {
                values.push(applyOp(ops.pop(), values.pop(), values.pop()));
            }
            return String.valueOf(values.pop());
        } catch (Exception e) {
            return "Error";
        }
    }

    private static boolean hasPrecedence(char op1, char op2) {
        if (op2 == '(' || op2 == ')') return false;
        return (op1 != '*' && op1 != '/') || (op2 != '+' && op2 != '-');
    }

    private static double applyOp(char op, double b, double a) {
        return switch (op) {
            case '+' -> a + b;
            case '-' -> a - b;
            case '*' -> a * b;
            case '/' -> {
                if (b == 0) throw new UnsupportedOperationException("Cannot divide by zero");
                yield a / b;
            }
            default -> 0;
        };
    }
}
