package co.edu.uptc.view;

import co.edu.uptc.pojo.Ball;
import co.edu.uptc.pojo.GameState;
import co.edu.uptc.pojo.Racket;
import co.edu.uptc.util.AppLogger;
import co.edu.uptc.view.utils.CustomPanel;

import javax.swing.JOptionPane;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GamePanel extends CustomPanel {
    private static final Logger LOGGER = AppLogger.getLogger(GamePanel.class);
    public interface InputListener {
        void onMoveUpPressed();
        void onMoveUpReleased();
        void onMoveDownPressed();
        void onMoveDownReleased();
        void onIncreaseSpeed();
        void onDecreaseSpeed();
    }

    private GameState gameState;
    private InputListener inputListener;
    private static final int PADDING = 10;

    public GamePanel() {
        initComponents();
        setPreferredSize(new Dimension(820, 620));
    }

    private void initComponents() {
        setLayout(null);
        setFocusable(true);
        setBackground(new Color(50, 50, 50));

        // KeyListener para movimiento de raqueta
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (inputListener == null) return;

                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    inputListener.onMoveUpPressed();
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    inputListener.onMoveDownPressed();
                } else if (e.getKeyCode() == KeyEvent.VK_PLUS || e.getKeyCode() == KeyEvent.VK_ADD) {
                    inputListener.onIncreaseSpeed();
                } else if (e.getKeyCode() == KeyEvent.VK_MINUS || e.getKeyCode() == KeyEvent.VK_SUBTRACT) {
                    inputListener.onDecreaseSpeed();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (inputListener == null) return;

                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    inputListener.onMoveUpReleased();
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    inputListener.onMoveDownReleased();
                }
            }
        });
    }

    public void setInputListener(InputListener inputListener) {
        this.inputListener = inputListener;
    }

    public void updateGame(GameState gameState) {
        this.gameState = gameState;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        try {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (gameState == null) return;

            // Dibujar área de juego
            drawGameArea(g2d);

            // Dibujar raqueta
            drawRacket(g2d);

            // Dibujar pelotas
            drawBalls(g2d);

            // Dibujar estado del juego
            drawGameStatus(g2d);
        } catch (Exception exception) {
            LOGGER.log(Level.SEVERE, "Error pintando GamePanel", exception);
        }
    }

    private void drawGameArea(Graphics2D g2d) {
        g2d.setColor(new Color(70, 70, 70));
        int fieldWidth = Math.min((int) gameState.getGameWidth(), getWidth() - 2 * PADDING);
        int fieldHeight = Math.min((int) gameState.getGameHeight(), getHeight() - 2 * PADDING);
        g2d.drawRect(PADDING, PADDING, fieldWidth - 2 * PADDING, fieldHeight - 2 * PADDING);
    }

    private void drawRacket(Graphics2D g2d) {
        Racket racket = gameState.getRacket();
        if (racket != null) {
            g2d.setColor(new Color(100, 150, 255)); // Azul
            g2d.fillRect(
                    PADDING + (int) racket.getX(),
                    PADDING + (int) racket.getY(),
                    (int) racket.getWidth(),
                    (int) racket.getHeight()
            );
            g2d.setColor(new Color(200, 200, 255));
            g2d.setStroke(new java.awt.BasicStroke(2));
            g2d.drawRect(
                    PADDING + (int) racket.getX(),
                    PADDING + (int) racket.getY(),
                    (int) racket.getWidth(),
                    (int) racket.getHeight()
            );
        }
    }

    private void drawBalls(Graphics2D g2d) {
        g2d.setColor(new Color(0, 200, 255)); // Cyan
        for (Ball ball : gameState.getBalls()) {
            if (ball.isActive()) {
                int x = PADDING + (int) (ball.getX() - ball.getRadius());
                int y = PADDING + (int) (ball.getY() - ball.getRadius());
                int diameter = (int) (ball.getRadius() * 2);
                g2d.fillOval(x, y, diameter, diameter);

                g2d.setColor(new Color(100, 255, 255));
                g2d.setStroke(new java.awt.BasicStroke(1));
                g2d.drawOval(x, y, diameter, diameter);
                g2d.setColor(new Color(0, 200, 255));
            }
        }
    }

    private void drawGameStatus(Graphics2D g2d) {
        g2d.setColor(Color.WHITE);
        g2d.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 14));
        String status = "Estado: " + gameState.getStatus().toString();
        g2d.drawString(status, 20, getHeight() - 10);
    }

    public boolean showGameOverDialog(String message) {
        LOGGER.info("Mostrando dialogo de game over");
        String[] options = {"Sí", "No"};
        int result = JOptionPane.showOptionDialog(
                this,
                message + "\n¿Deseas reiniciar?",
                "Game Over",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]
        );
            LOGGER.log(Level.INFO, "Usuario respondio game over con opcion {0}", result == JOptionPane.YES_OPTION ? "SI" : "NO");
        return result == JOptionPane.YES_OPTION;
    }
}
