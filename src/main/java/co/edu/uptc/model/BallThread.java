package co.edu.uptc.model;

import co.edu.uptc.pojo.Ball;
import co.edu.uptc.pojo.GameState;
import co.edu.uptc.pojo.GameStatus;
import co.edu.uptc.pojo.Racket;
import co.edu.uptc.util.AppLogger;

import java.util.logging.Level;
import java.util.logging.Logger;

public class BallThread extends Thread {
    private static final Logger LOGGER = AppLogger.getLogger(BallThread.class);
    private final Ball ball;
    private final GameState gameState;
    private volatile boolean running = true;
    private static final int FRAME_DELAY = 16; // ~60 FPS

    public BallThread(Ball ball, GameState gameState) {
        this.ball = ball;
        this.gameState = gameState;
        setName("BallThread-" + ball.getBallId());
        LOGGER.log(Level.FINE, "Creado hilo para pelota {0}", ball.getBallId());
    }

    @Override
    public void run() {
        while (running && ball.isActive()) {
            try {
                synchronized (gameState) {
                    if (gameState.getStatus() == GameStatus.PLAYING) {
                        updateBallPosition();
                        checkWallCollisions();
                        checkRacketCollision();
                    }
                }
                Thread.sleep(FRAME_DELAY);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                LOGGER.log(Level.WARNING, "BallThread interrupted for ball {0}", ball.getBallId());
                break;
            } catch (Exception exception) {
                LOGGER.log(Level.SEVERE, "Error inesperado en BallThread para pelota " + ball.getBallId(), exception);
                break;
            }
        }
    }

    private void updateBallPosition() {
        double speedMultiplier = gameState.getBallSpeedMultiplier();
        ball.setX(ball.getX() + ball.getVx() * speedMultiplier);
        ball.setY(ball.getY() + ball.getVy() * speedMultiplier);
    }

    private void checkWallCollisions() {
        double gameHeight = gameState.getGameHeight();
        double radius = ball.getRadius();

        // Rebote en paredes izquierda/derecha
        // Rebote en pared izquierda
        if (ball.getX() - radius <= 0) {
            ball.setVx(-ball.getVx());
            ball.setX(radius);
        }

        // Rebote en pared superior
        if (ball.getY() - radius <= 0) {
            ball.setVy(-ball.getVy());
            ball.setY(radius);
        }

        // Rebote en pared inferior
        if (ball.getY() + radius >= gameHeight) {
            ball.setVy(-ball.getVy());
            ball.setY(gameHeight - radius);
        }
    }

    private void checkRacketCollision() {
        Racket racket = gameState.getRacket();
        if (racket == null) {
            LOGGER.log(Level.WARNING, "Racket is null while processing ball {0}", ball.getBallId());
            return;
        }

        double ballX = ball.getX();
        double ballY = ball.getY();
        double ballRadius = ball.getRadius();

        // Colisión si la pelota toca a la raqueta (rebote)
        boolean collides = ballX - ballRadius <= racket.getX() + racket.getWidth()
                && ballX + ballRadius >= racket.getX()
                && ballY - ballRadius <= racket.getY() + racket.getHeight()
                && ballY + ballRadius >= racket.getY();

        if (collides) {
            // Rebote horizontal (la raqueta está a la derecha)
            ball.setVx(-Math.abs(ball.getVx()));
            ball.setBounces(ball.getBounces() + 1);
            // Ajustar posición para evitar pegar dentro de la raqueta
            ball.setX(racket.getX() - ballRadius - 1);
            LOGGER.log(Level.FINE, "Pelota {0} impacto raqueta, rebotes={1}", new Object[]{ball.getBallId(), ball.getBounces()});
        } else {
            // Si la pelota sobrepasa la posición de la raqueta (fallo), terminar juego
            if (ballX - ballRadius > racket.getX() + racket.getWidth()) {
                // La pelota pasó la raqueta
                ball.setActive(false);
                gameState.setStatus(GameStatus.GAME_OVER);
                LOGGER.log(Level.INFO, "Game over: ball {0} passed the racket", ball.getBallId());
            }
        }
    }

    public void stopBall() {
        running = false;
        LOGGER.log(Level.FINE, "Deteniendo hilo de pelota {0}", ball.getBallId());
    }
}
