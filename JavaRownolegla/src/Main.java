import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            EnhancedCalculatorApp app = new EnhancedCalculatorApp();
            app.setVisible(true);
        });
    }
}