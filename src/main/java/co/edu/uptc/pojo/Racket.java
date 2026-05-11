package co.edu.uptc.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Racket {
    private double x;
    private double y;
    private double width;
    private double height;
    private double minY;
    private double maxY;
}
