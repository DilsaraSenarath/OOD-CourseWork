import java.util.logging.*;

public class AppLogger {

    public static void setup() {
        // Get the global logger to configure it once
        Logger rootLogger = Logger.getLogger("");

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