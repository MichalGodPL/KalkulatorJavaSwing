import javax.swing.*;

import java.awt.*;

import java.awt.event.ActionEvent;

import java.awt.event.ActionListener;

import java.awt.event.MouseAdapter;

import java.awt.event.MouseEvent;

import java.util.Stack;


public class EnhancedCalculatorApp extends JFrame implements ActionListener {

    private final JTextField display; // Pole dla wyrażenia i wyniku

    private boolean showingResult = false; // Flaga do oznaczenia, czy wyświetlany jest wynik


    public EnhancedCalculatorApp() {

        setTitle("Aplikacja Kalkulator :3");

        setSize(400, 600);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLocationRelativeTo(null);

        setLayout(new BorderLayout(0, 0));


        // Ekran kalkulatora - jedno pole na wyrażenie i wynik

        display = new JTextField("0");

        display.setFont(new Font("Helvetica", Font.BOLD, 40));

        display.setHorizontalAlignment(JTextField.RIGHT);

        display.setEditable(false);

        display.setBackground(Color.BLACK);

        display.setForeground(Color.WHITE);

        display.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        add(display, BorderLayout.NORTH);


        JPanel buttonPanel = new JPanel(new GridLayout(5, 4, 10, 10));

        buttonPanel.setBackground(Color.BLACK);

        Color buttonColor = new Color(60, 60, 60);

        Color operatorColor = new Color(250, 149, 0);

        Color textColor = Color.WHITE;


        // Tablica przycisków bez przycisku +/-

        String[] buttonLabels = {

                "C", "", "", "÷",

                "7", "8", "9", "×",

                "4", "5", "6", "-",

                "1", "2", "3", "+",

                "0", ".", "=", ""

        };


        for (String label : buttonLabels) {

            RoundedButton button;

            if (label.matches("[+\\-×÷=]")) {

                button = new RoundedButton(label, operatorColor, textColor);

            } else {

                button = new RoundedButton(label, buttonColor, textColor);

            }

            button.setFont(new Font("Helvetica", Font.BOLD, 24));

            button.addActionListener(this);

            addHoverEffect(button, button.getBackground().brighter());

            buttonPanel.add(button);

        }

        add(buttonPanel, BorderLayout.CENTER);

    }

    private static class RoundedButton extends JButton {


        public RoundedButton(String text, Color baseColor, Color textColor) {

            super(text);

            setFocusPainted(false);

            setBackground(baseColor);

            setForeground(textColor);

            setOpaque(false);

            setContentAreaFilled(false);

            setBorderPainted(false);

        }

        @Override

        protected void paintComponent(Graphics g) {

            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(getBackground());

            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 50, 50);

            super.paintComponent(g2);

            g2.dispose();

        }

    }


    private void addHoverEffect(JButton button, Color hoverColor) {

        Color originalColor = button.getBackground();

        Timer hoverTimer = new Timer(5, null);

        button.addMouseListener(new MouseAdapter() {

            @Override

            public void mouseEntered(MouseEvent e) {

                hoverTimer.start();

            }


            @Override

            public void mouseExited(MouseEvent e) {

                hoverTimer.start();

            }


            @Override
            public void mousePressed(MouseEvent e) {

                button.setBackground(originalColor.darker());

            }


            @Override

            public void mouseReleased(MouseEvent e) {

                button.setBackground(hoverColor);

            }

        });

        hoverTimer.addActionListener(_ -> {

            Color targetColor = button.getModel().isRollover() ? hoverColor : originalColor;

            Color currentColor = button.getBackground();


            // Zwiększamy krok przesunięcia kolorów do 20%

            int r = (int) (currentColor.getRed() + 0.2 * (targetColor.getRed() - currentColor.getRed()));

            int g = (int) (currentColor.getGreen() + 0.2 * (targetColor.getGreen() - currentColor.getGreen()));

            int b = (int) (currentColor.getBlue() + 0.2 * (targetColor.getBlue() - currentColor.getBlue()));

            Color newColor = new Color(r, g, b);


            button.setBackground(newColor);

            button.repaint();


            if (newColor.equals(targetColor)) {

                hoverTimer.stop();

            }

        });

    }


    @Override

    public void actionPerformed(ActionEvent e) {

        String command = e.getActionCommand();

        if (showingResult) {  // Jeśli wynik był wyświetlony, zacznij od nowa

            display.setText("0");

            showingResult = false;

        }

        switch (command) {
            case "C" -> display.setText("0");
            case "=" -> {

                try {

                    String expression = display.getText();

                    String result = calculate(expression);

                    display.setText(result);

                    showingResult = true;  // Ustawiamy flagę, żeby przy następnym wpisaniu wyczyścić ekran

                } catch (Exception ex) {

                    display.setText("Error");

                    showingResult = true;

                }
            }
            case "." -> {

                String currentText = display.getText();

                String[] segments = currentText.split("[+\\-×÷]");

                String lastSegment = segments[segments.length - 1];

                if (!lastSegment.contains(".")) {

                    display.setText(display.getText() + ".");

                }
            }
            default -> {

                // Dodawanie tekstu do pola; resetuje pole, jeśli jest błąd lub zero

                if (display.getText().equals("0") || display.getText().equals("Error")) {

                    display.setText(command);

                } else {

                    display.setText(display.getText() + command);

                }
            }
        }
    }

    private String calculate(String expression) {

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

    private boolean hasPrecedence(char op1, char op2) {

        if (op2 == '(' || op2 == ')') return false;

        return (op1 != '*' && op1 != '/') || (op2 != '+' && op2 != '-');
    }


    private double applyOp(char op, double b, double a) {

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


    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            EnhancedCalculatorApp app = new EnhancedCalculatorApp();

            app.setVisible(true);

        });

    }

}
