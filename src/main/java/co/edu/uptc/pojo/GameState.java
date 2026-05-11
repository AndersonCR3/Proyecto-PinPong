package co.edu.uptc.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalTime;
import java.util.concurrent.CopyOnWriteArrayList;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameState {
    @Builder.Default
    private GameStatus status = GameStatus.STOPPED;
    
    @Builder.Default
    private CopyOnWriteArrayList<Ball> balls = new CopyOnWriteArrayList<>();
    
    private Racket racket;
    private LocalTime startTime;
    private long elapsedMillis;
    
    @Builder.Default
    private double gameWidth = 800;
    @Builder.Default
    private double gameHeight = 600;
    
    @Builder.Default
    private double ballSpeedMultiplier = 1.0;
}
