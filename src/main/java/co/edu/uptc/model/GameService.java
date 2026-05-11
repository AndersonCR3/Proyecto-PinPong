package co.edu.uptc.model;

import co.edu.uptc.interfaces.IGameService;
import co.edu.uptc.pojo.Ball;
import co.edu.uptc.pojo.GameState;
import co.edu.uptc.pojo.GameStatus;
import co.edu.uptc.pojo.Racket;
import co.edu.uptc.util.AppLogger;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameService implements IGameService {
    private static final Logger LOGGER = AppLogger.getLogger(GameService.class);
    private final GameState gameState;
    private final ExecutorService ballExecutor;
    private final ScheduledExecutorService timerExecutor;
    private final List<BallThread> ballThreads;
    private ScheduledFuture<?> timerTask;
    private int ballCounter = 0;
    private static final double RACKET_SPEED = 15.0;
    private static final double INITIAL_BALL_SPEED = 5.0;

    public GameService(GameState gameState) {
        this.gameState = gameState;
        this.ballExecutor = Executors.newCachedThreadPool();
        this.timerExecutor = Executors.newScheduledThreadPool(1);
        this.ballThreads = new ArrayList<>();
        initializeRacket();
        LOGGER.info("GameService inicializado");
    }

    private void initializeRacket() {
        Racket racket = Racket.builder()
                .x(gameState.getGameWidth() - 30)
                .y(gameState.getGameHeight() / 2 - 50)
                .width(20)
                .height(100)
                .minY(0)
                .maxY(gameState.getGameHeight() - 100)
                .build();
        gameState.setRacket(racket);
    }

    @Override
    public void startGame() {
        synchronized (gameState) {
            if (gameState.getStatus() != GameStatus.PLAYING) {
                LOGGER.info("Iniciando partida");
                gameState.setStatus(GameStatus.PLAYING);
                gameState.setStartTime(LocalTime.now());
                gameState.setElapsedMillis(0);

                // Crear primera pelota
                if (gameState.getBalls().isEmpty()) {
                    createBall();
                }

                // Iniciar timer
                startTimer();
            }
        }
    }

    @Override
    public void pauseGame() {
        synchronized (gameState) {
            if (gameState.getStatus() == GameStatus.PLAYING) {
                LOGGER.info("Partida pausada");
                gameState.setStatus(GameStatus.PAUSED);
            }
        }
    }

    @Override
    public void resumeGame() {
        synchronized (gameState) {
            if (gameState.getStatus() == GameStatus.PAUSED) {
                LOGGER.info("Partida reanudada");
                gameState.setStatus(GameStatus.PLAYING);
            }
        }
    }

    @Override
    public void restartGame() {
        synchronized (gameState) {
            LOGGER.info("Reiniciando partida");
            // Detener todos los threads de pelotas
            stopAllBalls();
            cancelTimer();
            gameState.getBalls().clear();
            ballThreads.clear();
            ballCounter = 0;

            // Reiniciar estado
            gameState.setStatus(GameStatus.STOPPED);
            gameState.setElapsedMillis(0);
            initializeRacket();
        }
    }

    @Override
    public void stopGame() {
        synchronized (gameState) {
            LOGGER.info("Deteniendo partida");
            gameState.setStatus(GameStatus.STOPPED);
            stopAllBalls();
            cancelTimer();
            gameState.getBalls().clear();
        }
    }

    @Override
    public void addBall() {
        synchronized (gameState) {
            if (gameState.getStatus() == GameStatus.PLAYING && gameState.getBalls().size() < 10) {
                LOGGER.info("Agregando nueva pelota");
                createBall();
            }
        }
    }

    private void createBall() {
        Ball ball = Ball.builder()
                .x(100 + (Math.random() * 200))
                .y(100 + (Math.random() * 200))
                .vx(INITIAL_BALL_SPEED * (Math.random() > 0.5 ? 1 : -1))
                .vy(INITIAL_BALL_SPEED * (Math.random() > 0.5 ? 1 : -1))
                .radius(10)
                .bounces(0)
                .active(true)
                .ballId(ballCounter++)
                .build();

        gameState.getBalls().add(ball);
        LOGGER.log(Level.INFO, "Pelota creada con id {0}", ball.getBallId());

        // Crear y lanzar thread para esta pelota
        BallThread ballThread = new BallThread(ball, gameState);
        ballThreads.add(ballThread);
        ballExecutor.execute(ballThread);
    }

    @Override
    public void increaseBallSpeed() {
        synchronized (gameState) {
            gameState.setBallSpeedMultiplier(Math.min(gameState.getBallSpeedMultiplier() + 0.2, 3.0));
        }
    }

    @Override
    public void decreaseBallSpeed() {
        synchronized (gameState) {
            gameState.setBallSpeedMultiplier(Math.max(gameState.getBallSpeedMultiplier() - 0.2, 0.5));
        }
    }

    @Override
    public void movePaddleUp() {
        Racket racket = gameState.getRacket();
        if (racket != null) {
            racket.setY(Math.max(racket.getY() - RACKET_SPEED, racket.getMinY()));
        }
    }

    @Override
    public void movePaddleDown() {
        Racket racket = gameState.getRacket();
        if (racket != null) {
            racket.setY(Math.min(racket.getY() + RACKET_SPEED, racket.getMaxY()));
        }
    }

    @Override
    public void updateGame() {
        synchronized (gameState) {
            // Limpiar pelotas inactivas
            gameState.getBalls().removeIf(ball -> !ball.isActive());
            checkGameOver();
        }
    }

    @Override
    public void checkGameOver() {
        if (gameState.getStatus() == GameStatus.PLAYING && gameState.getBalls().isEmpty()) {
            gameState.setStatus(GameStatus.GAME_OVER);
            stopAllBalls();
        }
    }

    private void startTimer() {
        if (timerTask != null && !timerTask.isCancelled() && !timerTask.isDone()) {
            return;
        }
        LOGGER.fine("Iniciando temporizador del juego");
        timerTask = timerExecutor.scheduleAtFixedRate(() -> {
            synchronized (gameState) {
                if (gameState.getStatus() == GameStatus.PLAYING) {
                    gameState.setElapsedMillis(gameState.getElapsedMillis() + 100);
                }
            }
        }, 0, 100, TimeUnit.MILLISECONDS);
    }

    private void cancelTimer() {
        if (timerTask != null) {
            LOGGER.fine("Cancelando temporizador del juego");
            timerTask.cancel(false);
            timerTask = null;
        }
    }

    private void stopAllBalls() {
        for (BallThread ballThread : ballThreads) {
            ballThread.stopBall();
        }
    }

    public void shutdown() {
        LOGGER.info("Apagando GameService");
        stopAllBalls();
        ballExecutor.shutdown();
        timerExecutor.shutdown();
    }
}
