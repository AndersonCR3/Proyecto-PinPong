package co.edu.uptc.interfaces;

import co.edu.uptc.pojo.GameState;

public interface IGameView {
    void showStartScreen();
    void showGameScreen();
    void renderGame(GameState gameState);
    void updateGameInfo(GameState gameState);
    void setPauseButtonText(String text);
    void requestGameFocus();
    void showInstructions();
    boolean askRestartAfterGameOver(String message);
    void closeApplication();
}
