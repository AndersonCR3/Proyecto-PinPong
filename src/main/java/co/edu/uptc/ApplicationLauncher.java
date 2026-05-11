package co.edu.uptc;

import co.edu.uptc.view.MainFrame;
import co.edu.uptc.util.AppLogger;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ApplicationLauncher {
    private static final Logger LOGGER = AppLogger.getLogger(ApplicationLauncher.class);

    public static void launch(String[] args) {
        AppLogger.configure();
        LOGGER.info("Iniciando aplicacion ProyectPP");
        SwingUtilities.invokeLater(() -> {
            try {
                FlatMacDarkLaf.setup();
                UIManager.put("Button.arc", 20);
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error configurando tema visual", e);
            }

            try {
                MainFrame mainFrame = MainFrame.getInstance();
                mainFrame.setVisible(true);
                mainFrame.openApp();
                LOGGER.info("Ventana principal mostrada correctamente");
            } catch (Exception exception) {
                LOGGER.log(Level.SEVERE, "Error inesperado iniciando la interfaz", exception);
            }
        });
    }
}
