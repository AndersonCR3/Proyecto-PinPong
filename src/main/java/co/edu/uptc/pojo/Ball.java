package co.edu.uptc.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Ball {
    private double x;
    private double y;
    private double vx; // velocidad en x
    private double vy; // velocidad en y
    private double radius;
    private int bounces;
    private boolean active;
    private int ballId; // identificador único
}
