package co.edu.uptc.view;

import co.edu.uptc.view.utils.CustomButton;
import co.edu.uptc.view.utils.CustomPanel;
import javax.swing.Box;
import javax.swing.BoxLayout;
import java.awt.Dimension;

public class ControlPanel extends CustomPanel {
    private CustomButton addBallBtn;
    private CustomButton pauseBtn;
    private CustomButton restartBtn;

    public ControlPanel() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(200, 120));

        add(Box.createVerticalStrut(10));

        addBallBtn = new CustomButton("Agregar pelota");
        addBallBtn.setMaximumSize(new Dimension(150, 40));
        add(addBallBtn);

        add(Box.createVerticalStrut(10));

        pauseBtn = new CustomButton("Pausar");
        pauseBtn.setMaximumSize(new Dimension(150, 40));
        add(pauseBtn);

        add(Box.createVerticalStrut(10));

        restartBtn = new CustomButton("Reiniciar");
        restartBtn.setMaximumSize(new Dimension(150, 40));
        add(restartBtn);

        add(Box.createVerticalGlue());
    }

    public CustomButton getAddBallBtn() {
        return addBallBtn;
    }

    public CustomButton getPauseBtn() {
        return pauseBtn;
    }

    public CustomButton getRestartBtn() {
        return restartBtn;
    }
}
