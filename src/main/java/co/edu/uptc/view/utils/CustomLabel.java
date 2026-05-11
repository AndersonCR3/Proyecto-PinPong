package co.edu.uptc.view.utils;

import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Font;

public class CustomLabel extends JLabel {
    public CustomLabel(String text) {
        super(text);
        initStyle();
    }

    private void initStyle() {
        setForeground(new Color(200, 200, 200)); // Gris claro
        setFont(new Font("Arial", Font.PLAIN, 12));
    }

    public void setHighlight(boolean highlight) {
        if (highlight) {
            setForeground(new Color(100, 200, 100)); // Verde claro
            setFont(new Font("Arial", Font.BOLD, 12));
        } else {
            setForeground(new Color(200, 200, 200));
            setFont(new Font("Arial", Font.PLAIN, 12));
        }
    }
}
