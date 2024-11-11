import java.util.Stack;

public class CalculatorLogic {
    public static String calculate(String expression) {
        expression = expression.replace("×", "*").replace("÷", "/");
        try {
            Stack<Double> values = new Stack<>();
            Stack<Character> ops = new Stack<>();
            for (int i = 0; i < expression.length(); i++) {
                char c = expression.charAt(i);
                if (Character.isDigit(c) || c == '.') {
                    StringBuilder buffer = new StringBuilder();
                    while (i < expression.length() && (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.')) {
                        buffer.append(expression.charAt(i++));
                    }
                    values.push(Double.parseDouble(buffer.toString()));
                    i--;
                } else if (c == '(') ops.push(c);
                else if (c == ')') {
                    while (ops.peek() != '(') values.push(applyOp(ops.pop(), values.pop(), values.pop()));
                    ops.pop();
                } else if ("+-*/".indexOf(c) >= 0) {
                    while (!ops.isEmpty() && hasPrecedence(c, ops.peek())) values.push(applyOp(ops.pop(), values.pop(), values.pop()));
                    ops.push(c);
                }
            }
            while (!ops.isEmpty()) values.push(applyOp(ops.pop(), values.pop(), values.pop()));
            return String.valueOf(values.pop());
        } catch (Exception e) {
            return "Error";
        }
    }

    private static boolean hasPrecedence(char op1, char op2) {
        return !(op2 == '(' || op2 == ')') && ((op1 != '*' && op1 != '/') || (op2 != '+' && op2 != '-'));
    }

    private static double applyOp(char op, double b, double a) {
        return op == '+' ? a + b : op == '-' ? a - b : op == '*' ? a * b : b == 0 ? Double.NaN : a / b;
    }
}
