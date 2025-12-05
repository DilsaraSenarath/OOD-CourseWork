import java.util.logging.*;

public class AppLogger {

    public static void setup() {
        // 1. Define the format using a system property.
        // %1$tF %1$tT = Date and Time (e.g., 2025-11-20 14:30:00)
        // %4$s = Log Level (e.g., INFO)
        // %5$s = The Message
        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tF %1$tT] [%4$s] %5$s %n");

        Logger rootLogger = Logger.getLogger("");

        // 2. Remove default handlers to prevent double logging
        for (Handler h : rootLogger.getHandlers()) {
            rootLogger.removeHandler(h);
        }

        // 3. Add a standard ConsoleHandler (it automatically uses the format above)
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(Level.ALL);

        rootLogger.addHandler(consoleHandler);
        rootLogger.setLevel(Level.INFO);
    }
}