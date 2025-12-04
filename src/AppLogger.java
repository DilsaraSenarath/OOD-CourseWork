import java.util.logging.*;
import java.util.Date;
import java.text.SimpleDateFormat;

public class AppLogger {

    public static void setup() {
        // Get the global logger to configure it once
        Logger rootLogger = Logger.getLogger("");

        // Remove default handlers (which print ugly 2-line red text)
        Handler[] handlers = rootLogger.getHandlers();
        if (handlers.length > 0 && handlers[0] instanceof ConsoleHandler) {
            rootLogger.removeHandler(handlers[0]);
        }

        // Create a new Console Handler
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(Level.ALL);

        // Define a simple one-line format: "[LEVEL] Message"
        consoleHandler.setFormatter(new Formatter() {
            @Override
            public String format(LogRecord record) {
                return "[" + record.getLevel() + "] " + record.getMessage() + "\n";
            }
        });

        // Add our nice handler to the logger
        rootLogger.addHandler(consoleHandler);
        rootLogger.setLevel(Level.INFO); // Log INFO, WARNING, SEVERE
    }
}