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
        setLayout(new BorderLayout());

        display = new JTextField("0");
        display.setFont(new Font("Helvetica", Font.BOLD, 40));
        display.setHorizontalAlignment(JTextField.RIGHT);
        display.setEditable(false);
        display.setBackground(Color.BLACK);
        display.setForeground(Color.WHITE);
        display.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(display, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(5, 4, 10, 10));
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
            RoundedButton button = label.matches("[+\\-×÷=]") ?
                    new RoundedButton(label, operatorColor, textColor) :
                    new RoundedButton(label, buttonColor, textColor);
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

        if ("C".equals(command)) display.setText("0");
        else if ("=".equals(command)) {
            try {
                display.setText(CalculatorLogic.calculate(display.getText()));
                showingResult = true;
            } catch (Exception ex) {
                display.setText("Error");
                showingResult = true;
            }
        } else if (".".equals(command)) {
            if (!display.getText().split("[+\\-×÷]")[display.getText().split("[+\\-×÷]").length - 1].contains("."))
                display.setText(display.getText() + ".");
        } else {
            display.setText(display.getText().equals("0") || display.getText().equals("Error") ?
                    command : display.getText() + command);
        }
    }
}
