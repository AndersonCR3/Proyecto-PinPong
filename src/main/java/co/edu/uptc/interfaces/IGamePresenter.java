package co.edu.uptc.interfaces;

public interface IGamePresenter {
    void onStartRequested();
    void onInstructionsRequested();
    void onExitRequested();
    void onAddBallRequested();
    void onPauseResumeRequested();
    void onRestartRequested();
    void onMoveUpPressed();
    void onMoveUpReleased();
    void onMoveDownPressed();
    void onMoveDownReleased();
    void onIncreaseSpeedRequested();
    void onDecreaseSpeedRequested();
    void shutdown();
}
