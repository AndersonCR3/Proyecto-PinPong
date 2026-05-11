package co.edu.uptc.presenter;

import co.edu.uptc.interfaces.IGamePresenter;
import co.edu.uptc.interfaces.IGameService;
import co.edu.uptc.interfaces.IGameView;
import co.edu.uptc.pojo.GameState;
import co.edu.uptc.pojo.GameStatus;
import co.edu.uptc.util.AppLogger;

import javax.swing.SwingUtilities;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GamePresenter implements IGamePresenter {
    private static final Logger LOGGER = AppLogger.getLogger(GamePresenter.class);
    private final IGameView gameView;
    private final IGameService gameService;
    private final GameState gameState;

    private ScheduledExecutorService renderExecutor;
    private volatile boolean renderLoopStarted = false;
    private volatile boolean gameOverDialogShown = false;
    private volatile boolean movingUp = false;
    private volatile boolean movingDown = false;

    public GamePresenter(IGameView gameView, IGameService gameService, GameState gameState) {
        this.gameView = gameView;
        this.gameService = gameService;
        this.gameState = gameState;
    }

    @Override
    public void onStartRequested() {
        LOGGER.info("Evento de inicio solicitado");
        gameView.showGameScreen();
        startMatch();
    }

    @Override
    public void onInstructionsRequested() {
        LOGGER.info("Evento de instrucciones solicitadas");
        gameView.showInstructions();
    }

    @Override
    public void onExitRequested() {
        LOGGER.info("Evento de salida solicitado");
        shutdown();
        gameView.closeApplication();
    }

    @Override
    public void onAddBallRequested() {
        LOGGER.info("Evento agregar pelota solicitado");
        gameService.addBall();
        gameView.requestGameFocus();
    }

    @Override
    public void onPauseResumeRequested() {
        if (gameState.getStatus() == GameStatus.PLAYING) {
            LOGGER.info("Pausando partida");
            gameService.pauseGame();
            gameView.setPauseButtonText("Reanudar");
        } else if (gameState.getStatus() == GameStatus.PAUSED) {
            LOGGER.info("Reanudando partida");
            gameService.resumeGame();
            gameView.setPauseButtonText("Pausar");
        }
        gameView.requestGameFocus();
    }

    @Override
    public void onRestartRequested() {
        LOGGER.info("Reinicio solicitado");
        startMatch();
    }

    @Override
    public void onMoveUpPressed() {
        LOGGER.fine("Raqueta arriba presionada");
        movingUp = true;
    }

    @Override
    public void onMoveUpReleased() {
        LOGGER.fine("Raqueta arriba liberada");
        movingUp = false;
    }

    @Override
    public void onMoveDownPressed() {
        LOGGER.fine("Raqueta abajo presionada");
        movingDown = true;
    }

    @Override
    public void onMoveDownReleased() {
        LOGGER.fine("Raqueta abajo liberada");
        movingDown = false;
    }

    @Override
    public void onIncreaseSpeedRequested() {
        LOGGER.fine("Aumento de velocidad solicitado");
        gameService.increaseBallSpeed();
    }

    @Override
    public void onDecreaseSpeedRequested() {
        LOGGER.fine("Disminucion de velocidad solicitada");
        gameService.decreaseBallSpeed();
    }

    private void startMatch() {
        LOGGER.info("Iniciando nueva partida");
        gameService.restartGame();
        gameService.startGame();
        gameView.setPauseButtonText("Pausar");
        gameView.requestGameFocus();

        if (!renderLoopStarted) {
            startRenderLoop();
            renderLoopStarted = true;
        }
    }

    private void startRenderLoop() {
        renderExecutor = Executors.newScheduledThreadPool(1);
        renderExecutor.scheduleAtFixedRate(() -> {
            try {
                if (movingUp) {
                    gameService.movePaddleUp();
                }
                if (movingDown) {
                    gameService.movePaddleDown();
                }

                gameService.updateGame();

                if (gameState.getStatus() == GameStatus.GAME_OVER) {
                    if (!gameOverDialogShown) {
                        gameOverDialogShown = true;
                        SwingUtilities.invokeLater(() -> {
                            boolean restart = gameView.askRestartAfterGameOver("¡Game Over! Una pelota escapó.");
                            if (restart) {
                                startMatch();
                            } else {
                                gameService.stopGame();
                                gameView.showStartScreen();
                                gameView.setPauseButtonText("Pausar");
                            }
                            gameOverDialogShown = false;
                        });
                    }
                    return;
                }

                SwingUtilities.invokeLater(() -> {
                    gameView.updateGameInfo(gameState);
                    gameView.renderGame(gameState);
                });
            } catch (Exception exception) {
                LOGGER.log(Level.SEVERE, "Error en el ciclo principal del juego", exception);
            }
        }, 0, 16, TimeUnit.MILLISECONDS);
    }

    @Override
    public void shutdown() {
        LOGGER.info("Apagando GamePresenter y servicios asociados");
        if (renderExecutor != null) {
            renderExecutor.shutdown();
        }
        gameService.shutdown();
    }
}
