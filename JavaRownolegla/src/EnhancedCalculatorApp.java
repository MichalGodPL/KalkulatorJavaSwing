import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EnhancedCalculatorApp extends JFrame implements ActionListener {
    private final JTextField display;
    private boolean showingResult = false;

    public EnhancedCalculatorApp() {
        setTitle("Aplikacja Kalkulator :3");
        setSize(400, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(0, 0));

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
            buttonPanel.add(button);
        }
        add(buttonPanel, BorderLayout.CENTER);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (showingResult) {
            display.setText("0");
            showingResult = false;
        }

        switch (command) {
            case "C" -> display.setText("0");
            case "=" -> {
                try {
                    String expression = display.getText();
                    String result = CalculatorLogic.calculate(expression);
                    display.setText(result);
                    showingResult = true;
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
                if (display.getText().equals("0") || display.getText().equals("Error")) {
                    display.setText(command);
                } else {
                    display.setText(display.getText() + command);
                }
            }
        }
    }
}
