package Game.tests;

import java.io.File;
import java.io.IOException;
import java.util.logging.*;

public class TestLogger {
    public static final String TEST_LOGS = "C:"+ File.separator +"Users" + File.separator +"shura"+ File.separator +"IdeaProjects"+ File.separator +"untitled"+ File.separator +"src"+ File.separator +"Game"+ File.separator+ "saves"+ File.separator + "test_logs.log";

    private static final Logger logger = Logger.getLogger(TestLogger.class.getName());

    static {
        try {
            // Настройка логгера
            LogManager.getLogManager().reset();
            logger.setLevel(Level.ALL);

            // Консольный handler
            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setLevel(Level.INFO);
            logger.addHandler(consoleHandler);

            // Файловый handler
            FileHandler fileHandler = new FileHandler(TEST_LOGS, true);
            fileHandler.setLevel(Level.ALL);
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Не удалось настроить логгер", e);
        }
    }

    public static Logger getLogger() {
        return logger;
    }
}
