package co.edu.uptc.view;

import co.edu.uptc.pojo.Ball;
import co.edu.uptc.pojo.GameState;
import co.edu.uptc.view.utils.CustomLabel;
import co.edu.uptc.view.utils.CustomPanel;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

public class GameInfoPanel extends CustomPanel {
    private CustomLabel titleLabel;
    private JTextArea ballInfoArea;
    private CustomLabel startTimeLabel;
    private CustomLabel elapsedTimeLabel;

    public GameInfoPanel() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(200, 600));

        // Título
        titleLabel = new CustomLabel("Game Info");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(Box.createVerticalStrut(10));
        add(titleLabel);
        add(Box.createVerticalStrut(10));

        // Rebotes por pelota
        JLabel ballsHeader = new CustomLabel("Rebotes por pelota:");
        ballsHeader.setFont(new Font("Arial", Font.BOLD, 12));
        add(ballsHeader);

        ballInfoArea = new JTextArea();
        ballInfoArea.setEditable(false);
        ballInfoArea.setBackground(new Color(30, 30, 30));
        ballInfoArea.setForeground(new Color(100, 200, 100));
        ballInfoArea.setFont(new Font("Courier", Font.PLAIN, 11));
        JScrollPane scrollPane = new JScrollPane(ballInfoArea);
        scrollPane.setPreferredSize(new Dimension(180, 150));
        add(scrollPane);
        add(Box.createVerticalStrut(15));

        // Hora de inicio
        JLabel startTimeHeader = new CustomLabel("Hora de inicio:");
        startTimeHeader.setFont(new Font("Arial", Font.BOLD, 12));
        add(startTimeHeader);

        startTimeLabel = new CustomLabel("--:--:--");
        add(startTimeLabel);
        add(Box.createVerticalStrut(15));

        // Tiempo transcurrido
        JLabel elapsedHeader = new CustomLabel("Tiempo transcurrido:");
        elapsedHeader.setFont(new Font("Arial", Font.BOLD, 12));
        add(elapsedHeader);

        elapsedTimeLabel = new CustomLabel("00:00:00");
        add(elapsedTimeLabel);
        add(Box.createVerticalGlue());
    }

    public void updateInfo(GameState gameState) {
        // Actualizar rebotes
        StringBuilder ballInfo = new StringBuilder();
        for (Ball ball : gameState.getBalls()) {
            if (ball.isActive()) {
                ballInfo.append(String.format("Pelota %d: %d\n", ball.getBallId(), ball.getBounces()));
            }
        }
        ballInfoArea.setText(ballInfo.toString());

        // Actualizar hora de inicio
        if (gameState.getStartTime() != null) {
            startTimeLabel.setText(gameState.getStartTime().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss")));
        }

        // Actualizar tiempo transcurrido
        long millis = gameState.getElapsedMillis();
        long seconds = (millis / 1000) % 60;
        long minutes = (millis / 1000 / 60) % 60;
        long hours = (millis / 1000 / 60 / 60);
        elapsedTimeLabel.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
    }
}
