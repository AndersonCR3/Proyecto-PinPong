package co.edu.uptc.view.utils;

import javax.swing.JButton;
import java.awt.Color;
import java.awt.Font;

public class CustomButton extends JButton {
    public CustomButton(String text) {
        super(text);
        initStyle();
    }

    private void initStyle() {
        setBackground(new Color(76, 175, 80)); // Verde
        setForeground(Color.WHITE);
        setFont(new Font("Arial", Font.BOLD, 14));
        setFocusPainted(false);
        setBorderPainted(false);
        setOpaque(true);
        setContentAreaFilled(true);
    }
}
