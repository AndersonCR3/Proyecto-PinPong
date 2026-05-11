package co.edu.uptc.interfaces;

public interface IGameService {
    void startGame();
    void pauseGame();
    void resumeGame();
    void restartGame();
    void addBall();
    void increaseBallSpeed();
    void decreaseBallSpeed();
    void movePaddleUp();
    void movePaddleDown();
    void updateGame();
    void checkGameOver();
    void stopGame();
    void shutdown();
}
