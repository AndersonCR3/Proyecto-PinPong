package co.edu.uptc.view;

import co.edu.uptc.interfaces.IGameView;
import co.edu.uptc.model.GameService;
import co.edu.uptc.pojo.GameState;
import co.edu.uptc.presenter.GamePresenter;
import co.edu.uptc.util.AppLogger;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import javax.swing.WindowConstants;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainFrame extends JFrame implements IGameView {
    private static final String CARD_START = "start";
    private static final String CARD_GAME = "game";
    private static final Logger LOGGER = AppLogger.getLogger(MainFrame.class);

    private static MainFrame instance;
    private JPanel rootPanel;
    private CardLayout cardLayout;
    private StartPanel startPanel;
    private JPanel gameContainer;
    private GamePanel gamePanel;
    private GameInfoPanel gameInfoPanel;
    private ControlPanel controlPanel;
    private GameService gameService;
    private GamePresenter gamePresenter;
    private GameState gameState;

    private MainFrame() {
        initFrame();
    }

    public static synchronized MainFrame getInstance() {
        if (instance == null) {
            instance = new MainFrame();
        }
        return instance;
    }

    private void initFrame() {
        setTitle("Ping-Pong Game");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        LOGGER.info("Inicializando MainFrame");

        // Inicializar modelo
        gameState = GameState.builder()
                .gameWidth(800)
                .gameHeight(600)
                .build();

        // Inicializar vistas
        startPanel = new StartPanel();
        gamePanel = new GamePanel();
        gameInfoPanel = new GameInfoPanel();
        controlPanel = new ControlPanel();

        // Inicializar servicio y presenter
        gameService = new GameService(gameState);
        gamePresenter = new GamePresenter(this, gameService, gameState);

        // Wirear GamePanel con Presenter (MVP estricto)
        gamePanel.setInputListener(new GamePanel.InputListener() {
            @Override
            public void onMoveUpPressed() {
                gamePresenter.onMoveUpPressed();
            }

            @Override
            public void onMoveUpReleased() {
                gamePresenter.onMoveUpReleased();
            }

            @Override
            public void onMoveDownPressed() {
                gamePresenter.onMoveDownPressed();
            }

            @Override
            public void onMoveDownReleased() {
                gamePresenter.onMoveDownReleased();
            }

            @Override
            public void onIncreaseSpeed() {
                gamePresenter.onIncreaseSpeedRequested();
            }

            @Override
            public void onDecreaseSpeed() {
                gamePresenter.onDecreaseSpeedRequested();
            }
        });

        // Layout principal con dos pantallas: inicio y juego
        cardLayout = new CardLayout();
        rootPanel = new JPanel(cardLayout);

        gameContainer = new JPanel(new BorderLayout());
        gameContainer.add(gamePanel, BorderLayout.CENTER);
        gameContainer.add(gameInfoPanel, BorderLayout.EAST);
        gameContainer.add(controlPanel, BorderLayout.SOUTH);

        rootPanel.add(startPanel, CARD_START);
        rootPanel.add(gameContainer, CARD_GAME);
        setContentPane(rootPanel);

        cardLayout.show(rootPanel, CARD_START);

        pack();
        setLocationRelativeTo(null);
        setResizable(false);

        startPanel.getStartGameBtn().addActionListener(e -> {
            try {
                gamePresenter.onStartRequested();
                LOGGER.info("Usuario presiono Iniciar juego");
            } catch (Exception exception) {
                LOGGER.log(Level.SEVERE, "Error al iniciar juego desde boton", exception);
            }
        });

        startPanel.getInstructionsBtn().addActionListener(e -> {
            try {
                gamePresenter.onInstructionsRequested();
                LOGGER.info("Usuario abrio instrucciones");
            } catch (Exception exception) {
                LOGGER.log(Level.SEVERE, "Error mostrando instrucciones", exception);
            }
        });

        startPanel.getExitBtn().addActionListener(e -> {
            try {
                LOGGER.info("Usuario solicito salir de la aplicacion");
                gamePresenter.onExitRequested();
            } catch (Exception exception) {
                LOGGER.log(Level.SEVERE, "Error cerrando aplicacion", exception);
            }
        });

        // Listeners de botones
        controlPanel.getAddBallBtn().addActionListener(e -> {
            try {
                gamePresenter.onAddBallRequested();
                LOGGER.info("Usuario agrego una nueva pelota");
            } catch (Exception exception) {
                LOGGER.log(Level.SEVERE, "Error agregando pelota", exception);
            }
        });

        controlPanel.getPauseBtn().addActionListener(e -> {
            try {
                gamePresenter.onPauseResumeRequested();
                LOGGER.info("Usuario alterno pausa/reanudar");
            } catch (Exception exception) {
                LOGGER.log(Level.SEVERE, "Error alternando pausa/reanudar", exception);
            }
        });

        controlPanel.getRestartBtn().addActionListener(e -> {
            try {
                gamePresenter.onRestartRequested();
                LOGGER.info("Usuario reinicio la partida");
            } catch (Exception exception) {
                LOGGER.log(Level.SEVERE, "Error reiniciando partida", exception);
            }
        });

        // Dar focus al gamePanel para recibir eventos de teclado
        gamePanel.setFocusable(true);
        gamePanel.requestFocus();
    }

    @Override
    public void requestGameFocus() {
        SwingUtilities.invokeLater(() -> {
            gamePanel.requestFocusInWindow();
            gamePanel.requestFocus();
        });
    }

    @Override
    public void showStartScreen() {
        LOGGER.info("Mostrando pantalla de inicio");
        cardLayout.show(rootPanel, CARD_START);
    }

    @Override
    public void showGameScreen() {
        LOGGER.info("Mostrando pantalla de juego");
        cardLayout.show(rootPanel, CARD_GAME);
    }

    @Override
    public void renderGame(GameState gameState) {
        gamePanel.updateGame(gameState);
    }

    @Override
    public void updateGameInfo(GameState gameState) {
        gameInfoPanel.updateInfo(gameState);
    }

    @Override
    public void setPauseButtonText(String text) {
        controlPanel.getPauseBtn().setText(text);
    }

    @Override
    public void showInstructions() {
        startPanel.showInstructionsDialog(this);
    }

    @Override
    public boolean askRestartAfterGameOver(String message) {
        return gamePanel.showGameOverDialog(message);
    }

    @Override
    public void closeApplication() {
        LOGGER.info("Cerrando aplicacion desde MainFrame");
        dispose();
        System.exit(0);
    }

    public void openApp() {
        controlPanel.getPauseBtn().setText("Pausar");
        showStartScreen();
        requestGameFocus();
    }

    public void shutdown() {
        gamePresenter.shutdown();
    }

    @Override
    public void dispose() {
        shutdown();
        super.dispose();
    }
}
