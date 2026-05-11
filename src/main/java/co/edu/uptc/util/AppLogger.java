package co.edu.uptc.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public final class AppLogger {
    private static final String LOG_FILE = "logs/proyectpp.log";
    private static volatile boolean initialized = false;

    private AppLogger() {
    }

    public static void configure() {
        if (initialized) {
            return;
        }

        synchronized (AppLogger.class) {
            if (initialized) {
                return;
            }

            try {
                Path logPath = Paths.get(LOG_FILE).toAbsolutePath();
                Path parent = logPath.getParent();
                if (parent != null) {
                    Files.createDirectories(parent);
                }

                LogManager.getLogManager().reset();
                Logger rootLogger = Logger.getLogger("");
                rootLogger.setLevel(Level.SEVERE);

                Formatter formatter = new SimpleLineFormatter();

                ConsoleHandler consoleHandler = new ConsoleHandler();
                consoleHandler.setLevel(Level.SEVERE);
                consoleHandler.setFormatter(formatter);
                rootLogger.addHandler(consoleHandler);

                FileHandler fileHandler = new FileHandler(logPath.toString(), false);
                fileHandler.setLevel(Level.SEVERE);
                fileHandler.setFormatter(formatter);
                rootLogger.addHandler(fileHandler);

                Thread.setDefaultUncaughtExceptionHandler((thread, throwable) ->
                        Logger.getLogger("co.edu.uptc").log(Level.SEVERE,
                                "Uncaught exception in thread " + thread.getName(), throwable));

                initialized = true;
                Logger.getLogger(AppLogger.class.getName()).severe("Logging configurado en " + logPath);
            } catch (IOException exception) {
                System.err.println("No se pudo configurar logging: " + exception.getMessage());
            }
        }
    }

    public static Logger getLogger(Class<?> type) {
        configure();
        return Logger.getLogger(type.getName());
    }

    private static final class SimpleLineFormatter extends Formatter {
        @Override
        public String format(LogRecord record) {
            return String.format("%1$tF %1$tT [%2$s] %3$s - %4$s%n",
                    record.getMillis(),
                    record.getLevel().getName(),
                    record.getLoggerName(),
                    formatMessage(record));
        }
    }
}
