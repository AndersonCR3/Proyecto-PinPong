package co.edu.uptc.view;

import co.edu.uptc.view.utils.CustomButton;
import co.edu.uptc.view.utils.CustomPanel;
import co.edu.uptc.util.AppLogger;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.logging.Logger;

public class StartPanel extends CustomPanel {
    private static final Logger LOGGER = AppLogger.getLogger(StartPanel.class);
    private final CustomButton startGameBtn;
    private final CustomButton instructionsBtn;
    private final CustomButton exitBtn;

    public StartPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(1020, 740));
        setBorder(BorderFactory.createEmptyBorder(80, 80, 80, 80));

        JLabel title = new JLabel("PING PONG - Concurrencia");
        title.setFont(new Font("Arial", Font.BOLD, 42));
        title.setForeground(new Color(180, 220, 255));
        title.setAlignmentX(CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Un jugador | Java Swing | Hilos por pelota");
        subtitle.setFont(new Font("Arial", Font.PLAIN, 18));
        subtitle.setForeground(new Color(180, 180, 180));
        subtitle.setAlignmentX(CENTER_ALIGNMENT);

        startGameBtn = new CustomButton("Iniciar juego");
        startGameBtn.setAlignmentX(CENTER_ALIGNMENT);
        startGameBtn.setMaximumSize(new Dimension(260, 48));

        instructionsBtn = new CustomButton("Instrucciones");
        instructionsBtn.setAlignmentX(CENTER_ALIGNMENT);
        instructionsBtn.setMaximumSize(new Dimension(260, 48));

        exitBtn = new CustomButton("Salir");
        exitBtn.setAlignmentX(CENTER_ALIGNMENT);
        exitBtn.setMaximumSize(new Dimension(260, 48));

        add(Box.createVerticalGlue());
        add(title);
        add(Box.createVerticalStrut(12));
        add(subtitle);
        add(Box.createVerticalStrut(40));
        add(startGameBtn);
        add(Box.createVerticalStrut(14));
        add(instructionsBtn);
        add(Box.createVerticalStrut(14));
        add(exitBtn);
        add(Box.createVerticalGlue());
    }

    public CustomButton getStartGameBtn() {
        return startGameBtn;
    }

    public CustomButton getInstructionsBtn() {
        return instructionsBtn;
    }

    public CustomButton getExitBtn() {
        return exitBtn;
    }

    public void showInstructionsDialog(JFrame parent) {
        LOGGER.info("Mostrando instrucciones del juego");
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setOpaque(false);
        textArea.setForeground(new Color(220, 220, 220));
        textArea.setFont(new Font("Arial", Font.PLAIN, 14));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setText(
                "Mecanicas del juego:\n\n"
                        + "1) Mueve la raqueta con flechas Arriba/Abajo.\n"
                        + "2) Inicia con una pelota y puedes agregar mas con el boton.\n"
                        + "3) Ajusta velocidad con teclas + y -.\n"
                        + "4) Pausa/Reanuda desde el boton Pausar.\n"
                        + "5) Reiniciar inicia una partida nueva.\n"
                        + "6) Pierdes si una pelota sobrepasa la raqueta por la derecha.\n\n"
                        + "Tip: cada pelota corre en su propio hilo."
        );

        JPanel panel = new JPanel();
        panel.setBackground(new Color(45, 45, 45));
        panel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(textArea);

        JOptionPane optionPane = new JOptionPane(
                panel,
                JOptionPane.INFORMATION_MESSAGE,
                JOptionPane.DEFAULT_OPTION
        );
        JDialog dialog = optionPane.createDialog(parent, "Instrucciones");
        dialog.setModal(true);
        dialog.setVisible(true);
    }
}
